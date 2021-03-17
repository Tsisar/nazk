package ua.com.tsisar.nazk.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import ua.com.tsisar.nazk.R;
import ua.com.tsisar.nazk.search.SearchFilters;
import ua.com.tsisar.nazk.util.Date;

public class SearchFiltersActivity extends AppCompatActivity {
    private static final String TAG = "MyLog";

    private EditText editTextQuery;
    private EditText editTextYear;

    private Button buttonStartDate;
    private Button buttonEndDate;

    private String query = "";
    private int documentType = 0;
    private int declarationType = 0;
    private String declarationYear = "";
    private Date startDate;
    private Date endDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_filters);

        getExtra();

        initEditTextQuery();
        initEditTextYear();

        initSpinnerDocumentType();
        initSpinnerDeclarationType();

        buttonStartDate = findViewById(R.id.button_start_date);
        if(!startDate.isNull()){
            buttonStartDate.setText(startDate.toStringReverse());
        }

        buttonEndDate = findViewById(R.id.button_end_date);
        if(!endDate.isNull()){
            buttonEndDate.setText(endDate.toStringReverse());
        }
    }

    private void getExtra(){
        Intent intent = getIntent();
        query = intent.getStringExtra(SearchFilters.EXTRA_QUERY);
        declarationYear = intent.getStringExtra(SearchFilters.EXTRA_DECLARATION_YEAR);
        declarationType = intent.getIntExtra(SearchFilters.EXTRA_DECLARATION_TYPE, SearchFilters.DECLARATION_ALL);
        documentType = intent.getIntExtra(SearchFilters.EXTRA_DOCUMENT_TYPE, SearchFilters.DOCUMENT_ALL);
        startDate = new Date().setDate(intent.getStringExtra(SearchFilters.EXTRA_DT_START));
        endDate = new Date().setDate(intent.getStringExtra(SearchFilters.EXTRA_DT_END));
    }

    private void initEditTextQuery(){
        editTextQuery = findViewById(R.id.edit_text_query);
        editTextQuery.setText(query);
    }

    private void initEditTextYear(){
        editTextYear = findViewById(R.id.edit_text_year);
        editTextYear.setText(declarationYear);
        editTextYear.setFilters(new InputFilter[]{new InputFilter.LengthFilter(4)});
        editTextYear.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                int length = editable.toString().length();
                if(length != 0) {
                    int a = Integer.parseInt("2015".substring(0, length));
                    int b = Integer.parseInt(editable.toString());
                    int c = Integer.parseInt(Integer.toString(new Date().setCurrentDate().getYear()).substring(0, length));

                    if(a > b || b > c){
                        editable.delete(length-1, length);
                    }
                }
            }
        });
        editTextYear.setOnFocusChangeListener((view, hasFocus) -> {
            if (!hasFocus) {
                checkEditTextYear();
            }
        });
    }

    private void checkEditTextYear(){
        if(!editTextYear.getText().toString().isEmpty() && Integer.parseInt(editTextYear.getText().toString()) < 2015){
            String actualYear = Integer.toString(new Date().setCurrentDate().getYear());
            editTextYear.setText(actualYear);
        }
    }

    private void initSpinnerDocumentType(){
        Spinner spinner = findViewById(R.id.spinner_document_type);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.array_document_type));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(documentType);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

    private void initSpinnerDeclarationType(){
        Spinner spinner = findViewById(R.id.spinner_declaration_type);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.array_declaration_type));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(declarationType);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

    public void setStartDate(View view){
        Date date = startDate.isNull() ? new Date().setCurrentDate() : new Date().setDate(startDate);

        DatePickerDialog datePickerDialog =
                new DatePickerDialog(this, (datePicker, year, month, day) ->{
                    startDate.setDate(year, month, day);
                    buttonStartDate.setText(startDate.toStringReverse());
                }, date.getYear(), date.getMonth(), date.getDay());
        datePickerDialog.show();
    }

    public void setEndDate(View view){
        Date date = endDate.isNull() ? new Date().setCurrentDate() : new Date().setDate(endDate);

        DatePickerDialog datePickerDialog =
                new DatePickerDialog(this, (datePicker, year, month, day) ->{
                    endDate.setDate(year, month, day);
                    buttonEndDate.setText(endDate.toStringReverse());
                }, date.getYear(), date.getMonth(), date.getDay());
        datePickerDialog.show();
    }

    public void onFindClick(View view) {
        query = editTextQuery.getText().toString();
        checkEditTextYear();
        declarationYear = editTextYear.getText().toString();

        if(startDate.isNull() && !endDate.isNull())
            startDate.setDate("2010-01-01");
        if(!startDate.isNull() && endDate.isNull())
            endDate.setCurrentDate();

        //if startDate > endDate reverse it
        if(startDate.compareTo(endDate) > 0){
            Date tmp = new Date().setDate(endDate);
            endDate.setDate(startDate);
            startDate.setDate(tmp);
        }

        if(endDate.compareTo(new Date().setCurrentDate()) > 0){
            endDate.setCurrentDate();
        }

        Log.i(TAG, "query: " + query);
        Log.i(TAG, "documentType: " + documentType);
        Log.i(TAG, "declarationType: " + declarationType);
        Log.i(TAG, "declarationYear: " + declarationYear);
        Log.i(TAG, "startDate: " + startDate);
        Log.i(TAG, "endDate: " + endDate);


        Intent intent = new Intent();
        intent.putExtra(SearchFilters.EXTRA_QUERY, query);
        intent.putExtra(SearchFilters.EXTRA_DOCUMENT_TYPE, documentType);
        intent.putExtra(SearchFilters.EXTRA_DECLARATION_TYPE, declarationType);
        intent.putExtra(SearchFilters.EXTRA_DECLARATION_YEAR, declarationYear);
        intent.putExtra(SearchFilters.EXTRA_DT_START, startDate.toString());
        intent.putExtra(SearchFilters.EXTRA_DT_END, startDate.toString());
        setResult(RESULT_OK, intent);
        finish();
    }
}