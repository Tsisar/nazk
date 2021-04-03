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

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import ua.com.tsisar.nazk.App;
import ua.com.tsisar.nazk.R;
import ua.com.tsisar.nazk.SearchFiltersView;
import ua.com.tsisar.nazk.api.JsonError;
import ua.com.tsisar.nazk.dto.Answer;
import ua.com.tsisar.nazk.dto.Item;
import ua.com.tsisar.nazk.util.Date;

public class MainActivity extends AppCompatActivity implements SearchFiltersView.Listener{
    private static final String TAG = "MyLog";

    private static final int REQUEST_CODE_FILTERS = 1;
    private SearchView searchView;

    private CompositeDisposable compositeDisposable;

    LinearLayout linearLayout;

//    @Nullable
//    private static Long nullify(long l){
//        return l == 0 ? null : l;
//    }
//
//    @Nullable
//    private static Integer nullify(int i){
//        return i == 0 ? null : i;
//    }

    private static String nullify(String s){
        return s.isEmpty() ? null : s;
    }

    private static Long nullify(Date d){
        return d.isClear() ? null : d.toLong();
    }

    private <V extends Number> V nullify(V  value){
        return value.intValue() == 0 ? null : value;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        compositeDisposable = new CompositeDisposable();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        CollapsingToolbarLayout toolBarLayout = findViewById(R.id.toolbar_layout);
        toolBarLayout.setTitle(getTitle());

        linearLayout = findViewById(R.id.linear_layout_search_filters);
        drawFilters("Якась декларація", 1);
        drawFilters("Якийьь тип", 2);
        drawFilters("2021", 3);
        drawFilters("Пошуковий запит", 4);

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
                App.getFilters().setQuery(query);
                searchDeclarations();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.i(TAG, "onQueryTextChange: " + newText);
                App.getFilters().setQuery(newText);
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

            Log.i(TAG, "query: " + App.getFilters().getQuery());
            Log.i(TAG, "userDeclarantId: " + App.getFilters().getUserDeclarantId());
            Log.i(TAG, "documentType: " + App.getFilters().getDocumentType());
            Log.i(TAG, "declarationType: " + App.getFilters().getDeclarationType());
            Log.i(TAG, "declarationYear: " + App.getFilters().getDeclarationYear());
            Log.i(TAG, "startDate: " + App.getFilters().getStartDate().toString());
            Log.i(TAG, "endDate: " + App.getFilters().getEndDate().toString());
            Log.i(TAG, "page: " + App.getFilters().getPage());

            if(App.getFilters().getQuery() != null && !App.getFilters().getQuery().isEmpty()) {
                searchView.post(() -> searchView.setQuery(App.getFilters().getQuery(), false));
            }

            //TODO тимчасово
            searchDeclarations();
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
            showMessage("onSearchDeclarationsSuccess Exception: " + e.getMessage());
            showMessage("Error: " + JsonError.get(answer.getError()).getMessage());
        }
    }

    private void onFailure(Throwable throwable) {
        showMessage("onFailure: " + throwable.getMessage());
    }

    private void searchDeclarations(){
        compositeDisposable.add(App.getApi().searchDeclarations(
                nullify(App.getFilters().getQuery()),
                nullify(App.getFilters().getUserDeclarantId()),
                nullify(App.getFilters().getDocumentType()),
                nullify(App.getFilters().getDeclarationType()),
                nullify(App.getFilters().getDeclarationYear()),
                nullify(App.getFilters().getStartDate()),
                nullify(App.getFilters().getEndDate()),
                nullify(App.getFilters().getPage()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Answer>() {
                    @Override
                    public void accept(Answer answer) throws Exception {
                        MainActivity.this.onSearchDeclarationsSuccess(answer);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        MainActivity.this.onFailure(throwable);
                    }
                }));
    }

    private void showMessage(String message){
        Log.e(TAG, "Message: " + message);
//        new SnackBar.Builder(this)
//                .withMessage(message)
//                .withStyle(SnackBar.Style.ALERT)
//                .show();
    }

    private void drawFilters(String itemName, int itemType){
        linearLayout.addView(
                new SearchFiltersView(this)
                        .setItemName(itemName)
                        .setItemType(itemType));
    }

    @Override
    public void removeView(SearchFiltersView view) {
        Log.e(TAG, "removeView: " + view.getItemType());
    }
}