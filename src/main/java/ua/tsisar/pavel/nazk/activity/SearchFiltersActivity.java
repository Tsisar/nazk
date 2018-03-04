package ua.tsisar.pavel.nazk.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import ua.tsisar.pavel.nazk.R;
import ua.tsisar.pavel.nazk.search.SearchFilters;

public class SearchFiltersActivity extends AppCompatActivity {

    private static final String EXTRA_QUERY = "query";
    private static final String EXTRA_DECLARATION_YEAR = "DeclarationYear";
    private static final String EXTRA_DECLARATION_TYPE = "DeclarationType";
    private static final String EXTRA_DOCUMENT_TYPE = "DocumentType";

    private String query;
    private int declarationYear;
    private int declarationType;
    private int documentType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_filters);

        Intent intent = getIntent();
        query = intent.getStringExtra(EXTRA_QUERY);
        declarationYear = intent.getIntExtra(EXTRA_DECLARATION_YEAR, 0);
        declarationType = intent.getIntExtra(EXTRA_DECLARATION_TYPE, SearchFilters.DECLARATION_ALL);
        documentType = intent.getIntExtra(EXTRA_DOCUMENT_TYPE, SearchFilters.DOCUMENT_ALL);

        init();
    }

    private void init(){
        initDeclarationTypeSpinner();
        initDocumentTypeSpinner();

        EditText queryEditText = findViewById(R.id.query_EditText);
        queryEditText.setText(String.valueOf(query));

        EditText declarationYearEditText = findViewById(R.id.year_EditText);
        declarationYearEditText.setText(declarationYear == 0?"":String.valueOf(declarationYear));

        Button find = findViewById(R.id.find_Button);
        find.setOnClickListener((View v) -> {
                String query = queryEditText.getText().toString();
                String year = declarationYearEditText.getText().toString();
                Intent intent = new Intent();
                intent.putExtra(EXTRA_QUERY, query);
                intent.putExtra(EXTRA_DECLARATION_YEAR, year.length() == 0?0:Integer.valueOf(year));
                intent.putExtra(EXTRA_DECLARATION_TYPE, declarationType);
                intent.putExtra(EXTRA_DOCUMENT_TYPE, documentType);
                setResult(RESULT_OK, intent);
                finish();
            }
        );
    }

    private void initDeclarationTypeSpinner(){
        Spinner declarationTypeSpinner = findViewById(R.id.declaration_type_spinner);
        ArrayAdapter<String> declarationTypeAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.declaration_type_array));
        declarationTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        declarationTypeSpinner.setAdapter(declarationTypeAdapter);
        declarationTypeSpinner.setSelection(declarationType);
        declarationTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                declarationType = position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                declarationType = SearchFilters.DECLARATION_ALL;
            }
        });
    }

    private void initDocumentTypeSpinner(){
        Spinner documentTypeSpinner = findViewById(R.id.document_type_spinner);
        ArrayAdapter<String> documentTypeAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.document_type_array));
        documentTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        documentTypeSpinner.setAdapter(documentTypeAdapter);
        documentTypeSpinner.setSelection(documentType);
        documentTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                documentType = position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                documentType = SearchFilters.DOCUMENT_ALL;
            }
        });
    }
}
