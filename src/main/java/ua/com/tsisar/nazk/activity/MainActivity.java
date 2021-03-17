package ua.com.tsisar.nazk.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import java.util.Objects;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import ua.com.tsisar.nazk.App;
import ua.com.tsisar.nazk.R;
import ua.com.tsisar.nazk.dto.AnswerDTO;
import ua.com.tsisar.nazk.search.SearchFilters;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MyLog";

    private static final int REQUEST_CODE_FILTERS = 1;
    private SearchView searchView;

    private CompositeDisposable compositeDisposable;

    @Nullable
    private static Integer nullify(int i){
        if(i == 0) {
            return null;
        }
        return (i);
    }

    @Nullable
    private static String nullify(String s){
        if(s.isEmpty()) {
            return null;
        }
        return s;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        compositeDisposable = new CompositeDisposable();
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
        getMenuInflater().inflate(R.menu.main_menu, menu);

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
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.i(TAG, "onQueryTextChange: " + newText);
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
            intent.putExtra(SearchFilters.EXTRA_QUERY, "Порошенко");
            intent.putExtra(SearchFilters.EXTRA_DOCUMENT_TYPE, 1);
            intent.putExtra(SearchFilters.EXTRA_DECLARATION_TYPE, 2);
            intent.putExtra(SearchFilters.EXTRA_DECLARATION_YEAR, 2020);
            intent.putExtra(SearchFilters.EXTRA_DT_START, "2020-01-01");
            intent.putExtra(SearchFilters.EXTRA_DT_END, "2021-01-01");
            startActivityForResult(intent, REQUEST_CODE_FILTERS);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_FILTERS) {
            String query = data.getStringExtra(SearchFilters.EXTRA_QUERY);
            int userDeclarantId = data.getIntExtra(SearchFilters.EXTRA_USER_DECLARANT_ID, 0);
            int documentType = data.getIntExtra(SearchFilters.EXTRA_DOCUMENT_TYPE, SearchFilters.DOCUMENT_ALL);
            int declarationType = data.getIntExtra(SearchFilters.EXTRA_DECLARATION_TYPE, SearchFilters.DECLARATION_ALL);
            int declarationYear = data.getIntExtra(SearchFilters.EXTRA_DECLARATION_YEAR, 0);
            String startDate = data.getStringExtra(SearchFilters.EXTRA_DT_START);
            String endDate = data.getStringExtra(SearchFilters.EXTRA_DT_END);
            int page = data.getIntExtra(SearchFilters.EXTRA_PAGE, 0);

            Log.i(TAG, "query: " + query);
            Log.i(TAG, "userDeclarantId: " + userDeclarantId);
            Log.i(TAG, "documentType: " + documentType);
            Log.i(TAG, "declarationType: " + declarationType);
            Log.i(TAG, "declarationYear: " + declarationYear);
            Log.i(TAG, "startDate: " + startDate);
            Log.i(TAG, "endDate: " + endDate);
            Log.i(TAG, "page: " + page);

            if(query != null && !query.isEmpty()) {
                searchView.post(() -> searchView.setQuery(query, false));
            }

            compositeDisposable.add(App.getApi().searchDeclarations(
                    query,
                    userDeclarantId,
                    documentType,
                    declarationType,
                    declarationYear,
                    startDate,
                    endDate,
                    page)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::onSearchDeclarationsSuccess, this::onFailure));
        }
    }

    private void onSearchDeclarationsSuccess(AnswerDTO answer) {
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
            Log.e(TAG, "Items: " + answer.getData());
//            recyclerAdapter = new RecyclerAdapter(this, answer.getItems());
//            recyclerView.setAdapter(recyclerAdapter);
        }catch (Exception e){
            e.printStackTrace();
            showMessage(e.getMessage());
            Log.e(TAG, Objects.requireNonNull(e.getMessage()));
        }
    }

    private void onFailure(Throwable throwable) {
    }

    private void searchDeclarations(){
//        compositeDisposable.add(App.getApi().searchDeclarations(
//                nullable(App.getFilters().getQuery()),
//                nullable(App.getFilters().getUserDeclarantId()),
//                nullable(App.getFilters().getDocumentType()),
//                nullable(App.getFilters().getDeclarationType()),
//                nullable(App.getFilters().getDeclarationYear()),
//                nullable(App.getFilters().getStartDate()),
//                nullable(App.getFilters().getEndDate()),
//                nullable(App.getFilters().getPage()))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(this::onSearchDeclarationsSuccess,
//                        this::onFailure));
    }

    private void showMessage(String message){
        Log.e(TAG, "Message: " + message);
//        new SnackBar.Builder(this)
//                .withMessage(message)
//                .withStyle(SnackBar.Style.ALERT)
//                .show();
    }
}