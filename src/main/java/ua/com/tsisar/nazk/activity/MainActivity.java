package ua.com.tsisar.nazk.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import java.util.Objects;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import ua.com.tsisar.nazk.App;
import ua.com.tsisar.nazk.R;
import ua.com.tsisar.nazk.dto.AnswerDTO;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MyLog";
    private static final int REQUEST_CODE_FILTERS = 1;

    private SearchView searchView;

    private CompositeDisposable compositeDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        compositeDisposable = new CompositeDisposable();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint(getString(R.string.query_hint));

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        if(null!=searchManager ) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        }
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                searchDeclarations();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                App.getFilters().setQuery(newText);
                return true;
            }
        });
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (compositeDisposable != null && !compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
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

/*
    @Override
    public void onUpdateSearchFilters() {
//        scrollView.setVisibility(View.GONE);
        searchDeclarations();
        searchFiltersLinearLayout.removeAllViews();

        if(searchFilters.getDeclarationYear() != 0){
            drawFilters(String.format(getString(R.string.declaration_year),
                    searchFilters.getDeclarationYear()), ITEM_TYPE_DECLARATION_YEAR);
        }
        if(searchFilters.getDeclarationType() != SearchFilters.DECLARATION_ALL){
            drawFilters(String.format(getString(R.string.declaration_type),
                    getResources().obtainTypedArray(R.array.declaration_type_array).
                            getString(searchFilters.getDeclarationType())),
                    ITEM_TYPE_DECLARATION_TYPE);
        }
        if(searchFilters.getDocumentType() != SearchFilters.DOCUMENT_ALL) {
            drawFilters(String.format(getString(R.string.document_type),
                    getResources().obtainTypedArray(R.array.document_type_array).
                            getString(searchFilters.getDocumentType())),
                    ITEM_TYPE_DOCUMENT_TYPE);
        }
        if(searchFilters.getDtStart().length() != 0 || searchFilters.getDtEnd().length() != 0) {
            String dtStart = searchFilters.getDtStart();
            String dtEnd = searchFilters.getDtEnd();
            drawFilters(String.format(getString(R.string.date_start_end_full),
                    dtStart.length()!=0?getString(R.string.dt_start):"", dtStart,
                    dtEnd.length()!=0?getString(R.string.dt_end):"", dtEnd),
                    ITEM_TYPE_DATE_START_END);
        }


    }
*/
    private void searchDeclarations(){
//        swipeRefreshLayout.setRefreshing(true);
        compositeDisposable.add(App.getApi().searchDeclarations(
                nullable(App.getFilters().getQuery()),
                nullable(App.getFilters().getUserDeclarantId()),
                nullable(App.getFilters().getDocumentType()),
                nullable(App.getFilters().getDeclarationType()),
                nullable(App.getFilters().getDeclarationYear()),
                nullable(App.getFilters().getStartDate()),
                nullable(App.getFilters().getEndDate()),
                nullable(App.getFilters().getPage()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onSearchDeclarationsSuccess,
                        this::onFailure));
    }


    private void onSearchDeclarationsSuccess(AnswerDTO answer) {
        try {
//            swipeRefreshLayout.setRefreshing(false);

            if (answer.getCount() == 0) {
                Log.e(TAG, "Null Results");
//                searchResult.setText(getString(R.string.search_null));
//                showMessage(String.format(getString(R.string.refresh_finished), 0));
            } else {
                Log.i(TAG, "Results Ok");
                Log.i(TAG, "Знайдено документів: " + answer.getCount());
                if(answer.getNotice() != null)
                    Log.i(TAG, answer.getNotice());
                Log.i(TAG, "DeclarationYear: " + answer.getData().get(0).getDeclarationYear());
                Log.i(TAG, "Lastname: " + answer.getData().get(0).getData().getStep1().getData().getLastname());
//                Log.v(TAG, answer.toString());
//                int totalItems = answer.getPage().getTotalItems();
//                searchResult.setText(String.format(getString(R.string.search_result), totalItems));
//                showMessage(String.format(getString(R.string.refresh_finished), totalItems));
            }
//            recyclerAdapter = new RecyclerAdapter(this, answer.getItems());
//            recyclerView.setAdapter(recyclerAdapter);
        }catch (Exception e){
            e.printStackTrace();
//            showMessage(e.getMessage());
            Log.e(TAG, Objects.requireNonNull(e.getMessage()));
        }
    }

    private void onFailure(Throwable throwable) {
        Log.e(TAG, Objects.requireNonNull(throwable.getMessage()));
    }

    private String nullable(int i){
        if(i == 0)
            return null;
        return Integer.toString(i);
    }

    private String nullable(String s){
        if(s.length() == 0)
            return null;
        return s;
    }
}
