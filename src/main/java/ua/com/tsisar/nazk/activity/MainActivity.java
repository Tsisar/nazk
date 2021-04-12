package ua.com.tsisar.nazk.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import ua.com.tsisar.nazk.App;
import ua.com.tsisar.nazk.R;
import ua.com.tsisar.nazk.view.SearchFiltersView;
import ua.com.tsisar.nazk.api.JsonError;
import ua.com.tsisar.nazk.dto.Answer;
import ua.com.tsisar.nazk.filters.Type;
import ua.com.tsisar.nazk.recycler.RecyclerAdapter;
import ua.com.tsisar.nazk.recycler.RecyclerItemClickListener;

public class MainActivity extends AppCompatActivity implements SearchFiltersView.Listener, SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "MyLog";

    private static final String URL = "https://public.nazk.gov.ua/documents/";

    private static final int REQUEST_CODE_FILTERS = 1;
    private SearchView searchView;

    private CompositeDisposable compositeDisposable;

    private TextView textViewPage;
    private LinearLayout linearLayoutFilters;
    private LinearLayout linearLayoutPage;
    private RecyclerView recyclerView;
    private RecyclerAdapter recyclerAdapter;

    private SwipeRefreshLayout swipeRefreshLayout;
    private CoordinatorLayout coordinatorLayout;

    private int maxPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        compositeDisposable = new CompositeDisposable();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        CollapsingToolbarLayout toolBarLayout = findViewById(R.id.toolbar_layout);
        toolBarLayout.setTitle(getTitle());

        coordinatorLayout = findViewById(R.id.coordinator_layout_main);
        linearLayoutFilters = findViewById(R.id.linear_layout_search_filters);
        linearLayoutPage = findViewById(R.id.linear_layout_content_page);
        linearLayoutPage.setVisibility(View.GONE);
        textViewPage = findViewById(R.id.text_view_content_page);

        AppBarLayout appBarLayout = findViewById(R.id.app_bar);
        appBarLayout.addOnOffsetChangedListener((appBarLayout1, verticalOffset) -> {
            if (Math.abs(verticalOffset) == appBarLayout1.getTotalScrollRange()) {
                linearLayoutFilters.setVisibility(View.GONE);
            } else if (verticalOffset == 0) {
                linearLayoutFilters.setVisibility(View.VISIBLE);
            }
        });

        recyclerView = findViewById(R.id.recycler_view_item);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, (View view, int position) ->
                        openUri(URL + recyclerAdapter.getItem(position).getId()))
        );

        swipeRefreshLayout = findViewById(R.id.refresh);
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (compositeDisposable != null && !compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
    }

    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        updateSearchFilters();
        searchDeclarations();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.search).getActionView();

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default
        searchView.setQueryHint(getString(R.string.hint_query));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                Log.i(TAG, "onQueryTextSubmit: " + query);
                App.getFilters().query().set(query);
                App.getFilters().page().clear();
                searchDeclarations();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.i(TAG, "onQueryTextChange: " + newText);
                App.getFilters().query().set(newText);
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.filter) {
            Intent intent = new Intent(this, SearchFiltersActivity.class);
            startActivityForResult(intent, REQUEST_CODE_FILTERS);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_FILTERS) {

            Log.i(TAG, "query: " + App.getFilters().query().get());
            Log.i(TAG, "userDeclarantId: " + App.getFilters().userDeclarantId().get());
            Log.i(TAG, "documentType: " + App.getFilters().documentType().get());
            Log.i(TAG, "declarationType: " + App.getFilters().declarationType().get());
            Log.i(TAG, "declarationYear: " + App.getFilters().declarationYear().get());
            Log.i(TAG, "startDate: " + App.getFilters().period().startDate().toLong());
            Log.i(TAG, "endDate: " + App.getFilters().period().endDate().toLong());
            Log.i(TAG, "page: " + App.getFilters().page().get());

            searchView.post(() -> searchView.setQuery(App.getFilters().query().get(), false));
            updateSearchFilters();
        }
    }

    private void onSearchDeclarationsSuccess(Answer answer) {
        try {
            swipeRefreshLayout.setRefreshing(false);
            if (answer.getCount() == 0) {
//                searchResult.setText(getString(R.string.search_null));
                showMessage(String.format(getString(R.string.refresh_finished), 0));
            } else {
                if(App.getFilters().page().isClear()) {
                    if (answer.getNotice() != null) {
                        showMessage(answer.getNotice());
                    } else {
                        showMessage(String.format(getString(R.string.refresh_finished), answer.getCount()));
                    }
                }
//                Log.i(TAG, "DeclarationYear: " + answer.getData().get(0).getDeclarationYear());
//                Log.i(TAG, "Lastname: " + answer.getData().get(0).getData().getStep1().getData().getLastname());
//                int totalItems = answer.getPage().getTotalItems();
//                searchResult.setText(String.format(getString(R.string.search_result), totalItems));
            }
            showPage(answer.getCount());
            recyclerAdapter = new RecyclerAdapter(this, answer.getData());
            recyclerView.setAdapter(recyclerAdapter);
        }catch (Exception e){
            e.printStackTrace();
//            showMessage("onSearchDeclarationsSuccess Exception: " + e.getMessage());
            showMessage(JsonError.get(answer.getError()).getMessage());
        }
    }

    private void onFailure(Throwable throwable) {
        swipeRefreshLayout.setRefreshing(false);
        showMessage(throwable.getMessage());
    }

    private void searchDeclarations(){
        swipeRefreshLayout.setRefreshing(true);
        compositeDisposable.add(Objects.requireNonNull(App.getApi().searchDeclarations(
                App.getFilters().query().get(),
                App.getFilters().userDeclarantId().get(),
                App.getFilters().documentType().get(),
                App.getFilters().declarationType().get(),
                App.getFilters().declarationYear().get(),
                App.getFilters().period().startDate().toLong(),
                App.getFilters().period().endDate().toLong(),
                App.getFilters().page().get()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(MainActivity.this::onSearchDeclarationsSuccess,
                        MainActivity.this::onFailure));

    }

    private void showMessage(String message){
        Log.e(TAG, "Message: " + message);
        Snackbar.make(coordinatorLayout,message,Snackbar.LENGTH_LONG).show();
    }

    private void addView(String name, Type type){
        linearLayoutFilters.addView(
                new SearchFiltersView(this)
                        .setName(name)
                        .setType(type));
    }

    @Override
    public void removeView(SearchFiltersView view) {
        Log.e(TAG, "removeView: " + view.getType());
        App.getFilters().page().clear();
        switch (view.getType()){
//            case QUERY:
//                App.getFilters().query().clear();
//                searchView.post(() -> searchView.setQuery(null, false));
//                break;
            case DOCUMENT_TYPE:
                App.getFilters().documentType().clear();
                App.getFilters().declarationType().clear();
                break;
            case DECLARATION_YEAR:
                App.getFilters().declarationYear().clear();
                break;
            case PERIOD:
                App.getFilters().period().clear();
                break;
        }
        linearLayoutFilters.removeView(view);
        searchDeclarations();
    }

    private void updateSearchFilters() {
        linearLayoutFilters.removeAllViews();
//        if(!App.getFilters().query().isClear()){
//            addView(App.getFilters().query().get(), Type.QUERY);
//        }
        if(!App.getFilters().documentType().isClear()){
            addView(String.format(getString(R.string.filter_document_type), getResources().
                    getStringArray(R.array.array_document_type)
                            [App.getFilters().documentType().get()]) +
                    (App.getFilters().declarationType().isClear() ? "" :
                            String.format(getString(R.string.filter_declaration_type),
                                    getResources().getStringArray(R.array.array_declaration_type)
                                            [App.getFilters().declarationType().get()])),
                    Type.DOCUMENT_TYPE);
        }
        if(!App.getFilters().declarationYear().isClear()){
            addView(String.format(getString(R.string.filter_year),
                    App.getFilters().declarationYear().get().toString()), Type.DECLARATION_YEAR);
        }
        if(!App.getFilters().period().isClear()){
            addView(String.format(getString(R.string.filter_period),
                    App.getFilters().period().startDate().toString(),
                    App.getFilters().period().endDate().toString()), Type.PERIOD);
        }
        searchDeclarations();
    }

    private void showPage(int count){
        maxPage = (count-1)/100 +1;

        if(count > 100) {
            if(App.getFilters().page().isClear()){
                App.getFilters().page().set(1);
            }
            linearLayoutPage.setVisibility(View.VISIBLE);
            textViewPage.setText(String.format("Сторінка %s із %s", App.getFilters().page().get(), maxPage));
        }else {
            linearLayoutPage.setVisibility(View.GONE);
            App.getFilters().page().clear();
        }
    }

    public void toPage(View view){
        int currentPage = App.getFilters().page().get();

        if(view.getId() == R.id.button_next_page && currentPage < maxPage) {
            App.getFilters().page().set(currentPage + 1);
        }else if(view.getId() == R.id.button_prev_page && currentPage > 1){
            App.getFilters().page().set(currentPage - 1);
        }
        searchDeclarations();
    }

    private void openUri(String url){
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        }
        catch (Exception e){
            e.printStackTrace();
            showMessage(e.getMessage());
        }
    }

    @Override
    public void onRefresh() {
        App.getFilters().page().clear();
        searchDeclarations();
    }
}
