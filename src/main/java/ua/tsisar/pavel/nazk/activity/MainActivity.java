package ua.tsisar.pavel.nazk.activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.util.Linkify;
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

public class MainActivity extends AppCompatActivity
        implements SearchFiltersView.Listener, SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "MyLog";
    private static final String URL = "https://public.nazk.gov.ua/documents/";
    private static final int REQUEST_CODE_FILTERS = 1;

    private static final int ID_FILTER = R.id.filter;
    private static final int ID_FAVORITES = R.id.favorites;

    private SearchView searchView;
    private CompositeDisposable compositeDisposable;

    private TextView textViewInfo;
    private TextView textViewPage;
    private LinearLayout linearLayoutFilters;
    private LinearLayout linearLayoutPage;
    private RecyclerView recyclerView;
    private CollapsingToolbarLayout toolBarLayout;
    private SwipeRefreshLayout swipeRefreshLayout;
    private CoordinatorLayout coordinatorLayout;

    private int maxPage;
    private DBHelper dbHelper;
    private State state = State.MAIN;

    enum State {
        MAIN,
        FAVORITES
    }

    private void setState(State state) {
        this.state = state;
        if (state == State.MAIN) {
            toolBarLayout.setTitle(getString(R.string.app_name));
        } else {
            toolBarLayout.setTitle(getString(R.string.favorites));
        }
    }

    public MatrixCursor copyCursor(Cursor cursor) {
        if (cursor != null) {
            String[] columnNames = {"_id", "history_query"};
            MatrixCursor matrixCursor = new MatrixCursor(columnNames);
            int cols = cursor.getColumnCount();
            if (cursor.moveToFirst()) {
                do {
                    Object[] row = new Object[cols];
                    for (int col = 0; col < cols; col++) {
                        row[col] = cursor.getString(col);
                    }
                    matrixCursor.addRow(row);
                } while (cursor.moveToNext());
            }
            cursor.close();
            return matrixCursor;
        }
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        compositeDisposable = new CompositeDisposable();
        dbHelper = new DBHelper(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolBarLayout = findViewById(R.id.toolbar_layout);
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

        if (searchView.getSuggestionsAdapter().getCursor() != null &&
                searchView.getSuggestionsAdapter().getCursor() != null) {
            searchView.getSuggestionsAdapter().getCursor().close();
        }

        if (dbHelper != null) {
            dbHelper.close();
        }
    }

    @Override
    public void onBackPressed() {
        if (state == State.FAVORITES) {
            setState(State.MAIN);
            updateSearchFilters();
        } else {
            super.onBackPressed();
        }
    }

    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (App.getFilters().isClear()) {
            updateSearchFilters();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);

        CursorExAdapter cursorAdapter = new CursorExAdapter(this, null);
        cursorAdapter.setOnItemClickListener(new CursorExAdapter.onItemClickListener() {
            @Override
            public void onItemClick(String query) {
                searchView.setQuery(query, true);
            }

            @Override
            public void onItemDelete(String query) {
                dbHelper.deleteHistory(query);
                searchView.getSuggestionsAdapter().
                        changeCursor(copyCursor(dbHelper.loadHistory(App.getFilters().query().get())));
            }
        });

        searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSuggestionsAdapter(cursorAdapter);
        searchView.setIconifiedByDefault(true);
        searchView.setQueryHint(getString(R.string.hint_query));
        searchView.setOnSearchClickListener(v -> searchView.setQuery(App.getFilters().query().get(), false));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                App.getFilters().page().clear();
                App.getFilters().query().set(query);
                updateSearchFilters();
                dbHelper.saveHistory(query);
                menu.findItem(R.id.search).collapseActionView();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
//                App.getFilters().query().set(newText);
                searchView.getSuggestionsAdapter().changeCursor(copyCursor(dbHelper.loadHistory(newText)));
                return true;
            }
        });
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case ID_FILTER:
                startActivityForResult(new Intent(this, SearchFiltersActivity.class), REQUEST_CODE_FILTERS);
                return true;
            case ID_FAVORITES:
                loadFavorites();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadFavorites() {
        setState(State.FAVORITES);
        drawItems(dbHelper.getFavoritesList());
        showPage(100);
//        App.getFilters().clear();
        linearLayoutFilters.removeAllViews();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_FILTERS) {
            setState(State.MAIN);
//            try {
//                searchView.post(() -> searchView.setQuery(App.getFilters().query().get(), false));
//            } catch (NullPointerException e) {
//                showMessage(e.getMessage());
//            }
            updateSearchFilters();
        }
    }

    private void onSearchDeclarationsSuccess(Answer answer) {
        try {
            swipeRefreshLayout.setRefreshing(false);
            drawItems(answer.getData());

            if (App.getFilters().page().isClear()) {
                if (answer.getNotice() != null) {
                    showMessage(answer.getNotice());
                } else {
                    showMessage(String.format(getString(R.string.refresh_finished), answer.getCount()));
                    if (answer.getCount() == 0) {
                        textViewInfo.setVisibility(View.VISIBLE);
                        textViewInfo.setText(getString(R.string.nothing_found));
                    }
                }
            }
            showPage(answer.getCount());
        } catch (Exception e) {
            e.printStackTrace();
//            showMessage(e.getMessage());
            if (JsonError.get(answer.getError()) != null) {
                showMessage(getString(JsonError.get(answer.getError()).getMessage()));
            } else {
                showMessage(String.format(getString(R.string.json_error), answer.getError()));
            }
            Log.e(TAG, "JsonError: " + answer.getError());
        }
    }

    private void drawItems(List<Item> list) {
        textViewInfo.setVisibility(View.GONE);
        RecyclerAdapter recyclerAdapter = new RecyclerAdapter(this, list);
        recyclerAdapter.setOnItemClickListener(item -> openURL(URL + item.getId()));
        recyclerView.setAdapter(recyclerAdapter);
    }

    private void onFailure(Throwable throwable) {
        swipeRefreshLayout.setRefreshing(false);
        showMessage(throwable.getMessage());
    }

    private void searchDeclarations() {
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

    private void showMessage(String message) {
        Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }

    private void addView(String name, Type type) {
        linearLayoutFilters.addView(
                new SearchFiltersView(this)
                        .setName(name)
                        .setType(type));
    }

    @Override
    public void removeView(SearchFiltersView view) {
        App.getFilters().page().clear();
        switch (view.getType()) {
            case QUERY:
                App.getFilters().query().clear();
                searchView.post(() -> searchView.setQuery(null, false));
                break;
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
        if (!App.getFilters().query().isClear()) {
            addView(App.getFilters().query().get(), Type.QUERY);
        }
        if (!App.getFilters().documentType().isClear()) {
            addView(String.format(getString(R.string.filter_document_type), getResources().
                            getStringArray(R.array.array_document_type)
                            [App.getFilters().documentType().get()]) +
                            (App.getFilters().declarationType().isClear() ? "" :
                                    String.format(getString(R.string.filter_declaration_type),
                                            getResources().getStringArray(R.array.array_declaration_type)
                                                    [App.getFilters().declarationType().get()])),
                    Type.DOCUMENT_TYPE);
        }
        if (!App.getFilters().declarationYear().isClear()) {
            addView(String.format(getString(R.string.filter_year),
                    App.getFilters().declarationYear().get().toString()), Type.DECLARATION_YEAR);
        }
        if (!App.getFilters().period().isClear()) {
            addView(String.format(getString(R.string.filter_period),
                    App.getFilters().period().startDate().toString(),
                    App.getFilters().period().endDate().toString()), Type.PERIOD);
        }
        searchDeclarations();
    }

    private void showPage(int count) {
        maxPage = (count - 1) / 100 + 1;

        if (count > 100) {
            if (App.getFilters().page().isClear()) {
                App.getFilters().page().set(1);
            }
            linearLayoutPage.setVisibility(View.VISIBLE);
            textViewPage.setText(String.format(getString(R.string.page_format), App.getFilters().page().get(), maxPage));
        } else {
            linearLayoutPage.setVisibility(View.GONE);
            App.getFilters().page().clear();
        }
    }

    public void toPage(View view) {
        int currentPage = App.getFilters().page().get();

        if (view.getId() == R.id.button_next_page && currentPage < maxPage) {
            App.getFilters().page().set(currentPage + 1);
        } else if (view.getId() == R.id.button_prev_page && currentPage > 1) {
            App.getFilters().page().set(currentPage - 1);
        }
        searchDeclarations();
    }

    private void openURL(String url) {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        } catch (Exception e) {
            e.printStackTrace();
            showMessage(e.getMessage());
        }
    }

    @Override
    public void onRefresh() {
        if (state == State.MAIN) {
            App.getFilters().page().clear();
            searchDeclarations();
        } else {
            loadFavorites();
        }
    }
}
