package ua.tsisar.pavel.nazk.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.util.Linkify;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

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

import java.util.List;
import java.util.Objects;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import ua.tsisar.pavel.nazk.App;
import ua.tsisar.pavel.nazk.R;
import ua.tsisar.pavel.nazk.adapter.CursorExAdapter;
import ua.tsisar.pavel.nazk.adapter.RecyclerAdapter;
import ua.tsisar.pavel.nazk.api.JsonError;
import ua.tsisar.pavel.nazk.dto.Answer;
import ua.tsisar.pavel.nazk.dto.Item;
import ua.tsisar.pavel.nazk.filters.Type;
import ua.tsisar.pavel.nazk.util.DBHelper;
import ua.tsisar.pavel.nazk.view.SearchFiltersView;

public class MainActivity extends AppCompatActivity implements SearchFiltersView.Listener, SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "MyLog";

    private static final String URL = "https://public.nazk.gov.ua/documents/";
    private static final int REQUEST_CODE_FILTERS = 1;

    private SearchView searchView;

    private CompositeDisposable compositeDisposable;

    private TextView textViewInfo;
    private TextView textViewPage;
    private LinearLayout linearLayoutFilters;
    private LinearLayout linearLayoutPage;
    private RecyclerView recyclerView;

    private SwipeRefreshLayout swipeRefreshLayout;
    private CoordinatorLayout coordinatorLayout;

    private int maxPage;

    private DBHelper dbHelper;
    private CursorExAdapter cursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        compositeDisposable = new CompositeDisposable();

        dbHelper = new DBHelper(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        CollapsingToolbarLayout toolBarLayout = findViewById(R.id.toolbar_layout);
        toolBarLayout.setTitle(getTitle());

        coordinatorLayout = findViewById(R.id.coordinator_layout_main);
        linearLayoutFilters = findViewById(R.id.linear_layout_search_filters);
        linearLayoutPage = findViewById(R.id.linear_layout_content_page);
        linearLayoutPage.setVisibility(View.GONE);
        textViewPage = findViewById(R.id.text_view_content_page);
        textViewInfo = findViewById(R.id.info);
        Linkify.addLinks(textViewInfo, Linkify.ALL);

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

        swipeRefreshLayout = findViewById(R.id.refresh);
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (compositeDisposable != null && !compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }

        if(cursorAdapter != null && cursorAdapter.getCursor() != null) {
            cursorAdapter.getCursor().close();
        }
    }

//    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//        updateSearchFilters();
//        searchDeclarations();
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);

        cursorAdapter = new CursorExAdapter(getContext(), null);
        cursorAdapter.setOnItemClickListener(new CursorExAdapter.onItemClickListener() {
            @Override
            public void onItemClick(String query) {
                searchView.setQuery(query, true);
            }

            @Override
            public void onItemDelete(String query) {
                dbHelper.deleteHistory(query);
                //TODO changeCursor?
                cursorAdapter.changeCursor(dbHelper.loadHistory(App.getFilters().query().get()));
            }
        });

        searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSuggestionsAdapter(cursorAdapter);
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default
        searchView.setQueryHint(getString(R.string.hint_query));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                App.getFilters().query().set(query);
                App.getFilters().page().clear();
                searchDeclarations();
                dbHelper.saveHistory(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                App.getFilters().query().set(newText);
                //TODO changeCursor?
                Cursor newCursor = dbHelper.loadHistory(newText);
                Cursor oldCursor = cursorAdapter.swapCursor(newCursor);

                Log.w(TAG, "newText: " + newText);
                Log.w(TAG, "oldCursor: " + oldCursor);
                Log.w(TAG, "newCursor: " + newCursor);
                Log.w(TAG, "_______________________________________________________");
//                if(oldCursor != null && !oldCursor.isClosed()) {
//                    oldCursor.close();
//                }
                return true;
            }
        });

        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.filter:
                startActivityForResult(new Intent(this, SearchFiltersActivity.class),
                        REQUEST_CODE_FILTERS);
                return true;
            case R.id.favorites:
                drawItems(dbHelper.getFavoritesList());
                showPage(100);
                App.getFilters().clear();
                linearLayoutFilters.removeAllViews();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_FILTERS) {
            try {
                searchView.post(() -> searchView.setQuery(App.getFilters().query().get(), false));
            }catch (NullPointerException e){
                showMessage(e.getMessage());
            }
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
//                searchResult.setText(String.format(getString(R.string.search_result), totalItems));
            }
            drawItems(answer.getData());
            showPage(answer.getCount());
        }catch (Exception e){
            e.printStackTrace();
//            showMessage("onSearchDeclarationsSuccess Exception: " + e.getMessage());
            if(JsonError.get(answer.getError()) != null) {
                showMessage(JsonError.get(answer.getError()).getMessage());
            }else {
                showMessage("JsonError: " + answer.getError());
            }
        }
    }

    private void drawItems(List<Item> list){
        textViewInfo.setVisibility(View.GONE);
        RecyclerAdapter recyclerAdapter = new RecyclerAdapter(this, list);
        recyclerAdapter.setOnItemClickListener(item -> openURL(URL + item.getId()));
        recyclerView.setAdapter(recyclerAdapter);
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

    private void openURL(String url){
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
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

    private Context getContext() {
        return this;
    }
}
