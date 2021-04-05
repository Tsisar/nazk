package ua.com.tsisar.nazk.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import ua.com.tsisar.nazk.App;
import ua.com.tsisar.nazk.R;
import ua.com.tsisar.nazk.SearchFiltersView;
import ua.com.tsisar.nazk.api.JsonError;
import ua.com.tsisar.nazk.dto.Answer;
import ua.com.tsisar.nazk.dto.Item;
import ua.com.tsisar.nazk.filters.Type;

public class MainActivity extends AppCompatActivity implements SearchFiltersView.Listener {
    private static final String TAG = "MyLog";

    private static final int REQUEST_CODE_FILTERS = 1;
    private SearchView searchView;

    private CompositeDisposable compositeDisposable;

    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        compositeDisposable = new CompositeDisposable();

        Log.i(TAG, "query: " + App.getFilters().query().get());
        App.getFilters().query().clear();
        Log.i(TAG, "query: " + App.getFilters().query().get());
        App.getFilters().query().set("");
        Log.i(TAG, "query: " + App.getFilters().query().get());


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        CollapsingToolbarLayout toolBarLayout = findViewById(R.id.toolbar_layout);
        toolBarLayout.setTitle(getTitle());

        linearLayout = findViewById(R.id.linear_layout_search_filters);

        AppBarLayout appBarLayout = findViewById(R.id.app_bar);
        appBarLayout.addOnOffsetChangedListener((appBarLayout1, verticalOffset) -> {
            if (Math.abs(verticalOffset) == appBarLayout1.getTotalScrollRange()) {
                // Collapsed
                linearLayout.setVisibility(View.GONE);
            } else if (verticalOffset == 0) {
                // Expanded
                linearLayout.setVisibility(View.VISIBLE);
            } else {
                // Somewhere in between
            }
        });


    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (compositeDisposable != null && !compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default
        searchView.setQueryHint(getString(R.string.hint_query));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                Log.i(TAG, "onQueryTextSubmit: " + query);
                App.getFilters().query().set(query);
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
        if (id == R.id.action_settings) {
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
            Log.i(TAG, "startDate: " + App.getFilters().period().startDate().toSeconds());
            Log.i(TAG, "endDate: " + App.getFilters().period().endDate().toSeconds());
            Log.i(TAG, "page: " + App.getFilters().page().get());

            searchView.post(() -> searchView.setQuery(App.getFilters().query().get(), false));
            updateSearchFilters();
        }
    }

    private void onSearchDeclarationsSuccess(Answer answer) {
        try {
//            swipeRefreshLayout.setRefreshing(false);
            if (answer.getCount() == 0) {
//                searchResult.setText(getString(R.string.search_null));
                showMessage(String.format(getString(R.string.refresh_finished), 0));
            } else {
                if(answer.getNotice() != null) {
                    showMessage(answer.getNotice());
                } else {
                    showMessage(String.format(getString(R.string.refresh_finished), answer.getCount()));
                }
//                Log.i(TAG, "DeclarationYear: " + answer.getData().get(0).getDeclarationYear());
//                Log.i(TAG, "Lastname: " + answer.getData().get(0).getData().getStep1().getData().getLastname());
//                int totalItems = answer.getPage().getTotalItems();
//                searchResult.setText(String.format(getString(R.string.search_result), totalItems));
            }
            //Log.e(TAG, "Items: " + answer.getData());
            List<Item> items = answer.getData();
            for(Item item : items){
                Log.v(TAG, "Item: " + item.getLastname() + " "
                        + item.getFirstname() + " "
                        + item.getMiddlename() + " - "
                        + item.getWorkPost() + " - "
                        + item.getWorkPlace() + " - "
                        + item.getId());
            }
//            recyclerAdapter = new RecyclerAdapter(this, answer.getItems());
//            recyclerView.setAdapter(recyclerAdapter);
        }catch (Exception e){
            e.printStackTrace();
//            showMessage("onSearchDeclarationsSuccess Exception: " + e.getMessage());
            showMessage("Error: " + JsonError.get(answer.getError()).getMessage());
        }
    }

    private void onFailure(Throwable throwable) {
        showMessage("onFailure: " + throwable.getMessage());
    }

    private void searchDeclarations(){
        compositeDisposable.add(App.getApi().searchDeclarations(
                App.getFilters().query().get(),
                App.getFilters().userDeclarantId().get(),
                App.getFilters().documentType().get(),
                App.getFilters().declarationType().get(),
                App.getFilters().declarationYear().get(),
                App.getFilters().period().startDate().toLong(),
                App.getFilters().period().endDate().toLong(),
                App.getFilters().page().get())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(MainActivity.this::onSearchDeclarationsSuccess,
                        MainActivity.this::onFailure));

    }

    private void showMessage(String message){
        Log.e(TAG, "Message: " + message);
        Toast.makeText(getApplicationContext(),"Message: " + message, Toast.LENGTH_LONG).show();
//        new SnackBar.Builder(this)
//                .withMessage(message)
//                .withStyle(SnackBar.Style.ALERT)
//                .show();
    }

    private void addView(String name, Type type){
        linearLayout.addView(
                new SearchFiltersView(this)
                        .setItemName(name)
                        .setFilterType(type));
    }

    @Override
    public void removeView(SearchFiltersView view) {
        Log.e(TAG, "removeView: " + view.getFilterType());
        switch (view.getFilterType()){
//            case QUERY:
//                App.getFilters().query().clear();
//                searchView.post(() -> searchView.setQuery(null, false));
//                break;
            case USER_DECLARANT_ID:
                App.getFilters().userDeclarantId().clear();
                break;
            case DOCUMENT_TYPE:
                App.getFilters().documentType().clear();
                break;
            case DECLARATION_TYPE:
                App.getFilters().declarationType().clear();
                break;
            case DECLARATION_YEAR:
                App.getFilters().declarationYear().clear();
                break;
            case PERIOD:
                App.getFilters().period().clear();
                break;
            case PAGE:
                App.getFilters().page().clear();
                break;
        }
        linearLayout.removeView(view);
        searchDeclarations();
    }

    public void updateSearchFilters() {
        linearLayout.removeAllViews();
//        if(!App.getFilters().query().isClear()){
//            addView(App.getFilters().query().get(), Type.QUERY);
//        }
        if(!App.getFilters().documentType().isClear()){
            addView(String.format(getString(R.string.filter_document_type), getResources().
                    getStringArray(R.array.array_document_type)
                            [App.getFilters().documentType().get()]), Type.DOCUMENT_TYPE);
        }
        if(!App.getFilters().declarationType().isClear()){
            addView(String.format(getString(R.string.filter_declaration_type), getResources().
                    getStringArray(R.array.array_declaration_type)
                            [App.getFilters().declarationType().get()]), Type.DECLARATION_TYPE);
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
}
