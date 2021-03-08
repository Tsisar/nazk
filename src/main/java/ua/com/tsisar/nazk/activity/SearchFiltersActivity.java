package ua.com.tsisar.nazk.activity;

import android.app.DatePickerDialog;
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

import java.util.Calendar;
import java.util.TimeZone;

import ua.com.tsisar.nazk.App;
import ua.com.tsisar.nazk.R;
import ua.com.tsisar.nazk.search.SearchFilters;

public class SearchFiltersActivity extends AppCompatActivity{

    private static final String TAG = "MyLog";

    private EditText queryEditText;
    private EditText declarationYearEditText;

    private Button btnStart;
    private Button btnEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_filters);

        initSpinner(R.id.declaration_type_spinner, R.array.declaration_type_array,
                App.getFilters().getDeclarationType())
                .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        App.getFilters().setDeclarationType(position);
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                        App.getFilters().setDeclarationType(SearchFilters.DOCUMENT_ALL);
                }
            });

        initSpinner(R.id.document_type_spinner, R.array.document_type_array,
                App.getFilters().getDocumentType())
                .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        App.getFilters().setDocumentType(position);
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                        App.getFilters().setDocumentType(SearchFilters.DOCUMENT_ALL);
                    }
                });

        queryEditText = findViewById(R.id.query_EditText);
        queryEditText.setText(String.valueOf(App.getFilters().getQuery()));

        String startDate = App.getFilters().getStartDate();
        btnStart = findViewById(R.id.start_date_Button);
        if(startDate.length() != 0)
            btnStart.setText(App.getFilters().getStartDate());

        String endDate = App.getFilters().getEndDate();
        btnEnd = findViewById(R.id.end_date_Button);
        if(endDate.length() != 0)
            btnEnd.setText(endDate);

        int year = App.getFilters().getDeclarationYear();
        declarationYearEditText = findViewById(R.id.year_EditText);
        declarationYearEditText.setText(year==0?"":String.valueOf(year));
        declarationYearEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(4)});
        declarationYearEditText.addTextChangedListener(new TextWatcher() {
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
                    int c = Integer.parseInt(Integer.toString(new Date().getYear()).substring(0, length));

                    if(a <= b && b <= c){
                        Log.e(TAG, "OK");
                    }else{
                        Log.e(TAG, "Apply filter");
                        editable.delete(length-1, length);
                    }
                }
            }
        });
        declarationYearEditText.setOnFocusChangeListener((view, hasFocus) -> {
            if(!hasFocus && Integer.parseInt(declarationYearEditText.getText().toString()) < 2015){
                declarationYearEditText.setText("2015");
            }
        });
    }

    private Spinner initSpinner(int viewId, int arrayId, int selection){
        Spinner spinner = findViewById(viewId);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, getResources().getStringArray(arrayId));
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setSelection(selection);
        return spinner;
    }

    public void onFindClick(View view){
        App.getFilters().setQuery(queryEditText.getText().toString());
        App.getFilters().setDeclarationYear(Integer.parseInt(declarationYearEditText.getText().toString()));

        String startDate = App.getFilters().getStartDate();
        String endDate = App.getFilters().getEndDate();

        if(startDate.length() == 0 && endDate.length() != 0)
            App.getFilters().setStartDate("2000-01-01");
        if(endDate.length() == 0 && startDate.length() != 0)
            App.getFilters().setEndDate(getDate());

        Log.i(TAG, "query: " + App.getFilters().getQuery());
        Log.i(TAG, "documentType: " + App.getFilters().getDocumentType());
        Log.i(TAG, "declarationType: " + App.getFilters().getDeclarationType());
        Log.i(TAG, "declarationYear: " + App.getFilters().getDeclarationYear());
        Log.i(TAG, "startDate: " + App.getFilters().getStartDate());
        Log.i(TAG, "endDate: " + App.getFilters().getEndDate());

        finish();
    }

    public void setStartDate(View view){
        Date date = new Date(App.getFilters().getStartDate());
        DatePickerDialog datePickerDialog =
                new DatePickerDialog(this, (datePicker, year, month, day) ->{
                    String startDate = String.format(getString(R.string.date_format),year, month+1, day);
                    btnStart.setText(startDate);
                    App.getFilters().setStartDate(startDate);
                }, date.getYear(), date.getMonth(), date.getDay());
        datePickerDialog.show();
    }

    public void setEndDate(View view){
        Date date = new Date(App.getFilters().getEndDate());
        DatePickerDialog datePickerDialog =
                new DatePickerDialog(this, (datePicker, year, month, day) ->{
                    String endDate = String.format(getString(R.string.date_format),year, month+1, day);
                    btnEnd.setText(endDate);
                    App.getFilters().setEndDate(endDate);
                }, date.getYear(), date.getMonth(), date.getDay());
        datePickerDialog.show();
    }

    private String getDate(){
        Date date = new Date();
        return String.format(getString(R.string.date_format), date.getYear(), date.getMonth()+1, date.getDay());
    }



    private class Date {
        private final int year;
        private final int month;
        private final int day;


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

        Date(){
            Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
            this.year = calendar.get(Calendar.YEAR);
            this.month = calendar.get(Calendar.MONTH);
            this.day = calendar.get(Calendar.DAY_OF_MONTH);
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