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
import android.widget.DatePicker;
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

    private SearchFilters searchFilters;

    private Date startDate;
    private Date endDate;

    public static Integer tryParse(String string) {
        try {
            return Integer.parseInt(string);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

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
        if(!startDate.isClear()){
            buttonStartDate.setText(startDate.toString());
        }

        buttonEndDate = findViewById(R.id.button_end_date);
        if(!endDate.isClear()){
            buttonEndDate.setText(endDate.toString());
        }
    }

    private void getExtra(){
        Intent intent = getIntent();
        startDate = new Date().set(intent.getStringExtra(SearchFilters.EXTRA_DT_START));
        endDate = new Date().set(intent.getStringExtra(SearchFilters.EXTRA_DT_END));

        searchFilters = new SearchFilters(
        intent.getStringExtra(SearchFilters.EXTRA_QUERY),
        intent.getIntExtra(SearchFilters.EXTRA_USER_DECLARANT_ID, 0),
        intent.getIntExtra(SearchFilters.EXTRA_DOCUMENT_TYPE, SearchFilters.DOCUMENT_ALL),
        intent.getIntExtra(SearchFilters.EXTRA_DECLARATION_TYPE, SearchFilters.DECLARATION_ALL),
        intent.getIntExtra(SearchFilters.EXTRA_DECLARATION_YEAR, 0),
        startDate.toString(),
        endDate.toString(),
        intent.getIntExtra(SearchFilters.EXTRA_PAGE, 0)
        );
    }

    private void initEditTextQuery(){
        editTextQuery = findViewById(R.id.edit_text_query);
        editTextQuery.setText(searchFilters.getQuery());
    }

    private void initEditTextYear(){
        editTextYear = findViewById(R.id.edit_text_year);
        if(searchFilters.getDeclarationYear() > 0) {
            editTextYear.setText(String.valueOf(searchFilters.getDeclarationYear()));
        }
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
                    int a = tryParse("2015".substring(0, length));
                    int b = tryParse(editable.toString());
                    int c = tryParse(String.valueOf(new Date().now().getYear()).substring(0, length));

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
        if(!editTextYear.getText().toString().isEmpty() && tryParse(editTextYear.getText().toString()) < 2015){
            String actualYear = Integer.toString(new Date().now().getYear());
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
        spinner.setSelection(searchFilters.getDocumentType());
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                searchFilters.setDocumentType(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                searchFilters.setDocumentType(SearchFilters.DOCUMENT_ALL);
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
        spinner.setSelection(searchFilters.getDeclarationType());
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                searchFilters.setDeclarationType(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                searchFilters.setDeclarationType(SearchFilters.DECLARATION_ALL);
            }
        });
    }

    public void setStartDate(View view){
        Date date = startDate.isClear() ? new Date().now() : new Date().set(startDate);

        DatePickerDialog datePickerDialog =
                new DatePickerDialog(this, (datePicker, year, month, day) ->{
                    startDate.set(year, month, day);
                    buttonStartDate.setText(startDate.toString());
                }, date.getYear(), date.getMonth(), date.getDay());
        datePickerDialog.show();
    }

    public void setEndDate(View view){
        Date date = endDate.isClear() ? new Date().now() : new Date().set(endDate);

        DatePickerDialog datePickerDialog =
                new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        endDate.set(year, month, day);
                        buttonEndDate.setText(endDate.toString());
                    }
                }, date.getYear(), date.getMonth(), date.getDay());
        datePickerDialog.show();
    }

    public void onFindClick(View view) {
        checkEditTextYear();

        searchFilters.setQuery(editTextQuery.getText().toString());
        searchFilters.setDeclarationYear(tryParse(editTextYear.getText().toString()));

        if(startDate.isClear() && !endDate.isClear())
            startDate.set("2016-08-01");
        if(!startDate.isClear() && endDate.isClear())
            endDate.now();

        //if startDate > endDate reverse it
        if(startDate.compareTo(endDate) > 0){
            Date tmp = new Date().set(endDate);
            endDate.set(startDate);
            startDate.set(tmp);
        }

        if(endDate.compareTo(new Date().now()) > 0){
            endDate.now();
        }

        Log.i(TAG, "query: " + searchFilters.getQuery());
        Log.i(TAG, "userDeclarantId: " + searchFilters.getUserDeclarantId());
        Log.i(TAG, "documentType: " + searchFilters.getDocumentType());
        Log.i(TAG, "declarationType: " + searchFilters.getDeclarationType());
        Log.i(TAG, "declarationYear: " + searchFilters.getDeclarationYear());
        Log.i(TAG, "startDate: " + startDate);
        Log.i(TAG, "endDate: " + endDate);
        Log.i(TAG, "page: " + searchFilters.getPage());

        Intent intent = new Intent();
//        intent.putExtra(SearchFilters.EXTRA_QUERY, searchFilters.getQuery());
//        intent.putExtra(SearchFilters.EXTRA_USER_DECLARANT_ID, searchFilters.getUserDeclarantId());
//        intent.putExtra(SearchFilters.EXTRA_DOCUMENT_TYPE, searchFilters.getDocumentType());
//        intent.putExtra(SearchFilters.EXTRA_DECLARATION_TYPE, searchFilters.getDeclarationType());
//        intent.putExtra(SearchFilters.EXTRA_DECLARATION_YEAR, searchFilters.getDeclarationYear());
//        intent.putExtra(SearchFilters.EXTRA_DT_START, startDate.toString());
//        intent.putExtra(SearchFilters.EXTRA_DT_END, endDate.toString());
//        intent.putExtra(SearchFilters.EXTRA_PAGE, searchFilters.getPage());
        setResult(RESULT_OK, intent);
        finish();
    }
}