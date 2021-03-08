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
import ua.com.tsisar.nazk.search.SearchFilters;
import ua.com.tsisar.nazk.search.listener.SearchFiltersListener;
import ua.com.tsisar.nazk.search.constants.SearchFiltersConstants;

public class MainActivity extends AppCompatActivity implements
        SearchFiltersListener{

    private static final String TAG = "MyLog";
    private static final int REQUEST_CODE_FILTERS = 1;

    private CompositeDisposable compositeDisposable;

    private SearchFilters searchFilters = new SearchFilters(this);

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
        SearchView searchView = (SearchView) searchItem.getActionView();
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
                searchFilters.update();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchFilters.setQuery(newText);
                return true;
            }
        });
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dispose();
    }

    private void dispose() {
        if (compositeDisposable != null && !compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SearchFiltersActivity.class);
            intent.putExtra(SearchFiltersConstants.EXTRA_QUERY, searchFilters.getQuery());
            intent.putExtra(SearchFiltersConstants.EXTRA_USER_DECLARANT_ID, searchFilters.getUserDeclarantId());
            intent.putExtra(SearchFiltersConstants.EXTRA_DOCUMENT_TYPE, searchFilters.getDocumentType());
            intent.putExtra(SearchFiltersConstants.EXTRA_DECLARATION_TYPE, searchFilters.getDeclarationType());
            intent.putExtra(SearchFiltersConstants.EXTRA_DECLARATION_YEAR, searchFilters.getDeclarationYear());
            intent.putExtra(SearchFiltersConstants.EXTRA_START_DATE, searchFilters.getStartDate());
            intent.putExtra(SearchFiltersConstants.EXTRA_END_DATE, searchFilters.getEndDate());
            intent.putExtra(SearchFiltersConstants.EXTRA_PAGE, searchFilters.getPage());
            startActivityForResult(intent, REQUEST_CODE_FILTERS);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onUpdateSearchFilters() {
//        scrollView.setVisibility(View.GONE);
        searchDeclarations();
/*        searchFiltersLinearLayout.removeAllViews();

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

 */
    }

    private void searchDeclarations(){
//        swipeRefreshLayout.setRefreshing(true);
        compositeDisposable.add(App.getApi().searchDeclarations(
                nullable(searchFilters.getQuery()),
                nullable(searchFilters.getUserDeclarantId()),
                nullable(searchFilters.getDocumentType()),
                nullable(searchFilters.getDeclarationType()),
                nullable(searchFilters.getDeclarationYear()),
                nullable(searchFilters.getStartDate()),
                nullable(searchFilters.getEndDate()),
                nullable(searchFilters.getPage()))
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
