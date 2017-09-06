package ua.tsisar.pavel.nazk.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.mrengineer13.snackbar.SnackBar;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import ua.tsisar.pavel.nazk.App;
import ua.tsisar.pavel.nazk.R;
import ua.tsisar.pavel.nazk.search.SearchFiltersView;
import ua.tsisar.pavel.nazk.search.SearchFilters;
import ua.tsisar.pavel.nazk.adapter.RecyclerAdapter;
import ua.tsisar.pavel.nazk.dto.AnswerDTO;
import ua.tsisar.pavel.nazk.recycler.RecyclerItemClickListener;
import ua.tsisar.pavel.nazk.search.listener.SearchFiltersListener;

public class MainActivity extends AppCompatActivity implements SearchFiltersListener, SearchFiltersView.Listener{

    private static final int ITEM_TYPE_DECLARATION_YEAR = 0;
    private static final int ITEM_TYPE_DECLARATION_TYPE = 1;
    private static final int ITEM_TYPE_DOCUMENT_TYPE = 2;

    private static final int REQUEST_CODE_FILTERS = 1;

    private static final String EXTRA_QUERY = "query";
    private static final String EXTRA_DECLARATION_YEAR = "DeclarationYear";
    private static final String EXTRA_DECLARATION_TYPE = "DeclarationType";
    private static final String EXTRA_DOCUMENT_TYPE = "DocumentType";

    private CompositeDisposable compositeDisposable;
    private AlphaAnimation clickAnimation;
    private RecyclerView recyclerView;

    private TextView searchResult;
    private SearchFilters searchFilters = new SearchFilters(this);

    private LinearLayout searchFiltersLinearLayout;
    private RecyclerAdapter recyclerAdapter;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        compositeDisposable = new CompositeDisposable();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        clickAnimation = new AlphaAnimation(1F, 0.2F);

        searchResult = (TextView) findViewById(R.id.search_result_textView);
        searchResult.setText(getString(R.string.edr_info));

        recyclerView = (RecyclerView) findViewById(R.id.item_recyclerView);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, (View view, int position) -> {
                    view.startAnimation(clickAnimation);
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(recyclerAdapter.getItemDTO(position).getLinkPDF()));
                    startActivity(intent);
                })
        );

        searchFiltersLinearLayout = (LinearLayout) findViewById(R.id.search_filters_LinearLayout);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
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

                    searchFilters
                            .setQuery(query)
                            .setDeclarationYear(declarationYear)
                            .setDeclarationType(declarationType)
                            .setDocumentType(documentType)
                            .update();
                    break;
            }
        }
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
                searchFilters
                        .setQuery(query)
                        .update();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
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
                startActivityForResult(intent, REQUEST_CODE_FILTERS);
                break;
        }
        return super.onContextItemSelected(item);
    }

    private void searchDeclarations(){
        progressBar.setVisibility(View.VISIBLE);
        compositeDisposable.add(App.getApi().searchDeclarations(
               searchFilters.getQuery(),
               searchFilters.getDeclarationYear(),
               searchFilters.getDeclarationType(),
               searchFilters.getDocumentType())
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
        progressBar.setVisibility(View.GONE);
        if(answer.getPage() == null){
            searchResult.setText(getString(R.string.search_null));
        }else {
            searchResult.setText(String.format(
                    getString(R.string.search_result),
                    answer.getPage().getTotalItems()));
        }
        recyclerAdapter = new RecyclerAdapter(this, answer.getItems());
        recyclerView.setAdapter(recyclerAdapter);
    }

    private void onFailure(Throwable throwable){
        progressBar.setVisibility(View.GONE);
        new SnackBar.Builder(this)
                .withMessage(throwable.getMessage())
                .withStyle(SnackBar.Style.ALERT)
                .show();
    }

    @Override
    public void onUpdateSearchFilters() {
        searchDeclarations();
        searchFiltersLinearLayout.removeAllViews();

        drawDeclarationYear();
        drawDeclarationType();
        drawDocumentType();
    }

    private void drawDeclarationYear(){
        if(searchFilters.getDeclarationYear() != 0){
            searchFiltersLinearLayout.addView(
                    new SearchFiltersView(this)
                            .setItemName(
                                    String.format(
                                            getString(R.string.declaration_year),
                                            searchFilters.getDeclarationYear()))
                            .setItemType(ITEM_TYPE_DECLARATION_YEAR));
        }
    }

    private void drawDeclarationType() {
        if(searchFilters.getDeclarationType() != SearchFilters.DECLARATION_ALL){
            searchFiltersLinearLayout.addView(
                    new SearchFiltersView(this)
                            .setItemName(
                                    String.format(
                                            getString(R.string.declaration_type),
                                            getResources()
                                                    .obtainTypedArray(R.array.declaration_type_array)
                                                    .getString(searchFilters.getDeclarationType())
                                    )
                            )
                            .setItemType(ITEM_TYPE_DECLARATION_TYPE));
        }
    }

    private void drawDocumentType() {
        if(searchFilters.getDocumentType() != SearchFilters.DOCUMENT_ALL){
            searchFiltersLinearLayout.addView(
                    new SearchFiltersView(this)
                            .setItemName(
                                    String.format(
                                            getString(R.string.document_type),
                                            getResources()
                                                    .obtainTypedArray(R.array.document_type_array)
                                                    .getString(searchFilters.getDocumentType())
                                    )
                            )
                            .setItemType(ITEM_TYPE_DOCUMENT_TYPE));
        }
    }

    @Override
    public void removeView(SearchFiltersView view) {
        searchFiltersLinearLayout.removeView(view);
        if (view.getItemType() == ITEM_TYPE_DECLARATION_YEAR) {
            searchFilters.setDeclarationYear(0);
        } else if (view.getItemType() == ITEM_TYPE_DECLARATION_TYPE) {
            searchFilters.setDeclarationType(SearchFilters.DECLARATION_ALL);
        } else if (view.getItemType() == ITEM_TYPE_DOCUMENT_TYPE) {
            searchFilters.setDocumentType(SearchFilters.DOCUMENT_ALL);
        }
        searchFilters.update();
    }
}
