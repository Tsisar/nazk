package ua.com.tsisar.nazk.activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.TimeZone;

import ua.com.tsisar.nazk.R;
import ua.com.tsisar.nazk.search.SearchFilters;
import ua.com.tsisar.nazk.search.constants.SearchFiltersConstants;

public class SearchFiltersActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MyLog";

    private String query;
    private int userDeclarantId;
    private int documentType;
    private int declarationType;
    private int declarationYear;
    private String startDate;
    private String endDate;
    private int page;

    private EditText queryEditText;
    private EditText declarationYearEditText;

    Button btnStart;
    Button btnEnd;
    Button btnFind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_filters);

        getExtra();
        initDeclarationTypeSpinner();
        initDocumentTypeSpinner();

        queryEditText = findViewById(R.id.query_EditText);
        queryEditText.setText(String.valueOf(query));

        declarationYearEditText = findViewById(R.id.year_EditText);
        declarationYearEditText.setText(declarationYear == 0?"":String.valueOf(declarationYear));

        btnStart = findViewById(R.id.start_date_Button);
        btnStart.setOnClickListener(this);
        if(startDate.length() != 0)
            btnStart.setText(startDate);

        btnEnd = findViewById(R.id.end_date_Button);
        btnEnd.setOnClickListener(this);
        if(endDate.length() != 0)
            btnEnd.setText(endDate);

        btnFind = findViewById(R.id.find_Button);
        btnFind.setOnClickListener(this);
    }

    private void getExtra(){
        Intent intent = getIntent();

        query = intent.getStringExtra(SearchFiltersConstants.EXTRA_QUERY);
        userDeclarantId = intent.getIntExtra(SearchFiltersConstants.EXTRA_USER_DECLARANT_ID, 0);
        declarationType = intent.getIntExtra(SearchFiltersConstants.EXTRA_DECLARATION_TYPE, SearchFilters.DECLARATION_ALL);
        declarationYear = intent.getIntExtra(SearchFiltersConstants.EXTRA_DECLARATION_YEAR, 0);
        documentType = intent.getIntExtra(SearchFiltersConstants.EXTRA_DOCUMENT_TYPE, SearchFilters.DOCUMENT_ALL);
        startDate = intent.getStringExtra(SearchFiltersConstants.EXTRA_START_DATE);
        endDate = intent.getStringExtra(SearchFiltersConstants.EXTRA_END_DATE);
        page = intent.getIntExtra(SearchFiltersConstants.EXTRA_PAGE, 0);
    }

    private void initDeclarationTypeSpinner(){
        Spinner spinner = findViewById(R.id.declaration_type_spinner);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.declaration_type_array));
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
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

    private void initDocumentTypeSpinner(){
        Spinner spinner = findViewById(R.id.document_type_spinner);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.document_type_array));
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
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

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start_date_Button:
                setStartDate(view);
                break;
            case R.id.end_date_Button:
                setEndDate(view);
            break;
            case R.id.find_Button:
                onFindClick(view);
            break;
        }
    }

    public void onFindClick(View view){
        query = queryEditText.getText().toString();
        try {
            declarationYear = Integer.parseInt(declarationYearEditText.getText().toString());
        }catch (NumberFormatException e){
            declarationYear = 0;
        }

        Intent intent = new Intent(this, SearchFiltersActivity.class);
        intent.putExtra(SearchFiltersConstants.EXTRA_QUERY, query);
        intent.putExtra(SearchFiltersConstants.EXTRA_USER_DECLARANT_ID, userDeclarantId);
        intent.putExtra(SearchFiltersConstants.EXTRA_DOCUMENT_TYPE, documentType);
        intent.putExtra(SearchFiltersConstants.EXTRA_DECLARATION_TYPE, declarationType);
        intent.putExtra(SearchFiltersConstants.EXTRA_DECLARATION_YEAR, declarationYear);
        intent.putExtra(SearchFiltersConstants.EXTRA_START_DATE, startDate.length() == 0
                && endDate.length() != 0 ? "2000-01-01" : startDate);
        intent.putExtra(SearchFiltersConstants.EXTRA_END_DATE, endDate.length() == 0
                && startDate.length() != 0 ? getDate() : endDate);
        intent.putExtra(SearchFiltersConstants.EXTRA_PAGE, page);
        setResult(RESULT_OK, intent);

        Log.i(TAG, "query: " + query);
        Log.i(TAG, "userDeclarantId: " + userDeclarantId);
        Log.i(TAG, "documentType: " + documentType);
        Log.i(TAG, "declarationType: " + declarationType);
        Log.i(TAG, "declarationYear: " + declarationYear);
        Log.i(TAG, "startDate: " + startDate);
        Log.i(TAG, "endDate: " + endDate);
        Log.i(TAG, "page: " + page);

        finish();
    }

    public void setStartDate(View view){
        Date date = new Date(startDate);
        DatePickerDialog datePickerDialog =
                new DatePickerDialog(this, (datePicker, year, month, day) ->{
                    startDate = String.format(getString(R.string.date_format),year, month+1, day);
                    btnStart.setText(startDate);
                }, date.getYear(), date.getMonth(), date.getDay());
        datePickerDialog.show();
    }

    public void setEndDate(View view){
        Date date = new Date(endDate);
        DatePickerDialog datePickerDialog =
                new DatePickerDialog(this, (datePicker, year, month, day) ->{
                    endDate = String.format(getString(R.string.date_format),year, month+1, day);
                    btnEnd.setText(endDate);
                }, date.getYear(), date.getMonth(), date.getDay());
        datePickerDialog.show();
    }

    private String getDate(){
        Date date = new Date(null);
        return String.format(getString(R.string.date_format), date.getYear(), date.getMonth()+1, date.getDay());
    }

    private class Date {
        private int year;
        private int month;
        private int day;

        Date(String dateStartEnd){
            if(dateStartEnd == null || dateStartEnd.length() == 0){
                Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
                this.year = calendar.get(Calendar.YEAR);
                this.month = calendar.get(Calendar.MONTH);
                this.day = calendar.get(Calendar.DAY_OF_MONTH);
            }else{
                String[] splitArray = dateStartEnd.split(getString(R.string.split));
                this.year = Integer.parseInt(splitArray[0]);
                this.month = Integer.parseInt(splitArray[1])-1;
                this.day = Integer.parseInt(splitArray[2]);
            }
        }

        int getYear(){
            return year;
        }

        int getMonth(){
            return month;
        }

        int getDay(){
            return day;
        }
    }
}