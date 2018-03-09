package ua.tsisar.pavel.nazk.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mrengineer13.snackbar.SnackBar;


import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import ua.tsisar.pavel.nazk.App;
import ua.tsisar.pavel.nazk.dialog.OpenDialog;
import ua.tsisar.pavel.nazk.R;
import ua.tsisar.pavel.nazk.search.SearchFiltersView;
import ua.tsisar.pavel.nazk.search.SearchFilters;
import ua.tsisar.pavel.nazk.adapter.RecyclerAdapter;
import ua.tsisar.pavel.nazk.dto.AnswerDTO;
import ua.tsisar.pavel.nazk.recycler.RecyclerItemClickListener;
import ua.tsisar.pavel.nazk.search.listener.SearchFiltersListener;

public class MainActivity extends AppCompatActivity implements
        SearchFiltersListener, SearchFiltersView.Listener, SwipeRefreshLayout.OnRefreshListener{

    private static final int ITEM_TYPE_DECLARATION_YEAR = 0;
    private static final int ITEM_TYPE_DECLARATION_TYPE = 1;
    private static final int ITEM_TYPE_DOCUMENT_TYPE = 2;
    private static final int ITEM_TYPE_DATE_START_END = 3;

    private static final int REQUEST_CODE_FILTERS = 1;

    private static final String EXTRA_QUERY = "query";
    private static final String EXTRA_DECLARATION_YEAR = "DeclarationYear";
    private static final String EXTRA_DECLARATION_TYPE = "DeclarationType";
    private static final String EXTRA_DOCUMENT_TYPE = "DocumentType";
    private static final String EXTRA_DT_START = "dtStart";
    private static final String EXTRA_DT_END = "dtEnd";

    private static final String LINK_PDF = "LinkPDF";
    private static final String LINK_HTML = "LinkHTML";

    private static final String URL = "https://public.nazk.gov.ua/declaration/";


    private CompositeDisposable compositeDisposable;
    private AlphaAnimation clickAnimation;
    private RecyclerView recyclerView;

    private TextView searchResult;
    private SearchFilters searchFilters = new SearchFilters(this);

    private LinearLayout searchFiltersLinearLayout;
    private RecyclerAdapter recyclerAdapter;

    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        compositeDisposable = new CompositeDisposable();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        clickAnimation = new AlphaAnimation(1F, 0.2F);

        searchResult = findViewById(R.id.search_result_textView);
        searchResult.setText(getString(R.string.edr_info));

        recyclerView = findViewById(R.id.item_recyclerView);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, (View view, int position) -> {
                    view.startAnimation(clickAnimation);
                    OpenDialog dialog = new OpenDialog();
                    Bundle arguments = new Bundle();
                    arguments.putString(LINK_PDF, recyclerAdapter.getItemDTO(position).getLinkPDF());
                    arguments.putString(LINK_HTML, URL + recyclerAdapter.getItemDTO(position).getId());
                    dialog.setArguments(arguments);

                    dialog.show(getSupportFragmentManager(), "openDialog");
                })
        );

        swipeRefreshLayout = findViewById(R.id.refresh);
        swipeRefreshLayout.setOnRefreshListener(this);

        searchFiltersLinearLayout = findViewById(R.id.search_filters_LinearLayout);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dispose();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_FILTERS:
                    String query = data.getStringExtra(EXTRA_QUERY);
                    int declarationYear = data.getIntExtra(EXTRA_DECLARATION_YEAR, 0);
                    int declarationType = data.getIntExtra(EXTRA_DECLARATION_TYPE, SearchFilters.DECLARATION_ALL);
                    int documentType = data.getIntExtra(EXTRA_DOCUMENT_TYPE, SearchFilters.DOCUMENT_ALL);
                    String dtStart = data.getStringExtra(EXTRA_DT_START);
                    String dtEnd = data.getStringExtra(EXTRA_DT_END);

                    searchFilters.setQuery(query)
                                 .setDeclarationYear(declarationYear)
                                 .setDeclarationType(declarationType)
                                 .setDocumentType(documentType)
                                 .setDtStart(dtStart)
                                 .setDtEnd(dtEnd)
                                 .update();
                    break;
            }
        }
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        searchFilters.save(outState);
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        searchFilters.restore(savedInstanceState);
        if(!searchFilters.isNull())
            searchFilters.update();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);

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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(this, SearchFiltersActivity.class);
                intent.putExtra(EXTRA_QUERY, searchFilters.getQuery());
                intent.putExtra(EXTRA_DECLARATION_YEAR, searchFilters.getDeclarationYear());
                intent.putExtra(EXTRA_DECLARATION_TYPE, searchFilters.getDeclarationType());
                intent.putExtra(EXTRA_DOCUMENT_TYPE, searchFilters.getDocumentType());
                intent.putExtra(EXTRA_DT_START, searchFilters.getDtStart());
                intent.putExtra(EXTRA_DT_END, searchFilters.getDtEnd());
                startActivityForResult(intent, REQUEST_CODE_FILTERS);
                break;
        }
        return super.onContextItemSelected(item);
    }

    private void searchDeclarations(){
        swipeRefreshLayout.setRefreshing(true);
        compositeDisposable.add(App.getApi().searchDeclarations(
               searchFilters.getQuery(),
               searchFilters.getDeclarationYear(),
               searchFilters.getDeclarationType(),
               searchFilters.getDocumentType(),
               searchFilters.getDtStart(),
               searchFilters.getDtEnd())
               .subscribeOn(Schedulers.io())
               .observeOn(AndroidSchedulers.mainThread())
               .subscribe(this::onSearchDeclarationsSuccess,
                          this::onFailure));
    }

    private void dispose() {
        if (compositeDisposable != null && !compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
    }

    private void onSearchDeclarationsSuccess(AnswerDTO answer){
        try {
            swipeRefreshLayout.setRefreshing(false);
            showMessage(String.format(getString(R.string.refresh_finished), answer.getPage().getTotalItems()));
            if (answer.getPage() == null) {
                searchResult.setText(getString(R.string.search_null));
            } else {
                searchResult.setText(String.format(
                        getString(R.string.search_result),
                        answer.getPage().getTotalItems()));
            }
            recyclerAdapter = new RecyclerAdapter(this, answer.getItems());
            recyclerView.setAdapter(recyclerAdapter);
        }catch (Exception e){
            e.printStackTrace();
            showMessage(e.getMessage());
        }
    }

    private void onFailure(Throwable throwable){
        swipeRefreshLayout.setRefreshing(false);
        showMessage(throwable.getMessage());
    }

    @Override
    public void onUpdateSearchFilters() {
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

    private void drawFilters(String itemName, int itemType){
        searchFiltersLinearLayout.addView(
                new SearchFiltersView(this)
                        .setItemName(itemName)
                        .setItemType(itemType));
    }

    @Override
    public void removeView(SearchFiltersView view) {
        int viewType = view.getItemType();

        searchFiltersLinearLayout.removeView(view);

        switch (viewType){
            case ITEM_TYPE_DECLARATION_YEAR:
                searchFilters.setDeclarationYear(0);
                break;
            case ITEM_TYPE_DECLARATION_TYPE:
                searchFilters.setDeclarationType(SearchFilters.DECLARATION_ALL);
                break;
            case ITEM_TYPE_DOCUMENT_TYPE:
                searchFilters.setDocumentType(SearchFilters.DOCUMENT_ALL);
                break;
            case ITEM_TYPE_DATE_START_END:
                searchFilters.setDtStart("");
                searchFilters.setDtEnd("");
                break;
        }
        searchFilters.update();
    }

    private void showMessage(String message){
        new SnackBar.Builder(this)
                .withMessage(message)
                .withStyle(SnackBar.Style.ALERT)
                .show();
    }

    @Override
    public void onRefresh() {
        showMessage(getString(R.string.refresh_started));
        searchFilters.update();
    }
}
