package ua.com.tsisar.nazk.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

import ua.com.tsisar.nazk.App;
import ua.com.tsisar.nazk.R;
import ua.com.tsisar.nazk.search.SearchFilters;
import ua.com.tsisar.nazk.util.Date;

public class SearchFiltersActivity extends AppCompatActivity {
    private static final String TAG = "MyLog";

    private EditText editTextQuery;
    private EditText editTextYear;

    private Button buttonStartDate;
    private Button buttonEndDate;

//    private SearchFilters searchFilters;

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

        startDate = new Date();
        endDate = new Date();

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

    private void initEditTextQuery(){
        editTextQuery = findViewById(R.id.edit_text_query);
        editTextQuery.setText(App.getFilters().getQuery());
    }

    private void initEditTextYear(){
        editTextYear = findViewById(R.id.edit_text_year);
        if(App.getFilters().getDeclarationYear() > 0) {
            editTextYear.setText(String.valueOf(App.getFilters().getDeclarationYear()));
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
                    Integer year = new Date().now().getYear();
                    int a = tryParse("2015".substring(0, length));
                    int b = tryParse(editable.toString());
                    int c = tryParse(year.toString().substring(0, length));

                    if(a > b || b > c){
                        editable.delete(length-1, length);
                        Toast.makeText(getApplicationContext(),"2015 - " + year, Toast.LENGTH_SHORT).show();
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
            String actualYear = new Date().now().getYear().toString();
            editTextYear.setText(actualYear);
        }
    }

    private void initSpinnerDocumentType(){
        Spinner spinner = initSpinner(R.id.spinner_document_type, R.array.array_document_type);
        spinner.setSelection(App.getFilters().getDocumentType());
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                App.getFilters().setDocumentType(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                App.getFilters().setDocumentType(SearchFilters.DOCUMENT_ALL);
            }
        });
    }

    private void initSpinnerDeclarationType(){
        Spinner spinner = initSpinner(R.id.spinner_declaration_type, R.array.array_declaration_type);
        spinner.setSelection(App.getFilters().getDeclarationType());
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                App.getFilters().setDeclarationType(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                App.getFilters().setDeclarationType(SearchFilters.DECLARATION_ALL);
            }
        });
    }

    private Spinner initSpinner(int viewId, int arrayId){
        Spinner spinner = findViewById(viewId);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                getResources().getStringArray(arrayId));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        return spinner;
    }

    //TODO Період публікації зробити двома діалогами в любому випадку відкривати спочатку "З" потім "ПО". В разі не встановлення дати в якомусь діалозі - дату залишати пустою.
    // dialog.getDatePicker().setMaxDate(new Date().getTime());
    public void setStartDate(View view){
        Date date = startDate.isClear() ? new Date().now() : new Date().set(startDate);

        DatePickerDialog dialog = new DatePickerDialog(this, (datePicker, year, month, day) -> {
            buttonStartDate.setText(startDate.set(year, month, day).toString());
        }, date.getYear(), date.getMonth(), date.getDay());
        // 01.08.2016
        //dialog.getDatePicker().setMinDate(Calendar.getInstance().getTimeInMillis());
        // date now;
        dialog.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis());
        dialog.setTitle("setStartDate");
        dialog.show();
    }

    public void setEndDate(View view){
        Date date = endDate.isClear() ? new Date().now() : new Date().set(endDate);
        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                buttonEndDate.setText(endDate.set(year, month, day).toString());
            }
        }, date.getYear(), date.getMonth(), date.getDay());
        // setStartDate.toLong
        //dialog.getDatePicker().setMinDate(Calendar.getInstance().getTimeInMillis());
        dialog.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis());
        dialog.show();
    }

    public void onFindClick(View view) {
        checkEditTextYear();

        App.getFilters().setQuery(editTextQuery.getText().toString());
        App.getFilters().setDeclarationYear(tryParse(editTextYear.getText().toString()));

        if(startDate.isClear() && !endDate.isClear())
            startDate.set(2016,7,1);
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

        App.getFilters().setStartDate(startDate);
        App.getFilters().setEndDate(endDate);

//        Log.i(TAG, "query: " + App.getFilters().getQuery());
//        Log.i(TAG, "userDeclarantId: " + App.getFilters().getUserDeclarantId());
//        Log.i(TAG, "documentType: " + App.getFilters().getDocumentType());
//        Log.i(TAG, "declarationType: " + App.getFilters().getDeclarationType());
//        Log.i(TAG, "declarationYear: " + App.getFilters().getDeclarationYear());
//        Log.i(TAG, "startDate: " + App.getFilters().getStartDate().toString());
//        Log.i(TAG, "endDate: " + App.getFilters().getEndDate().toString());
//        Log.i(TAG, "page: " + App.getFilters().getPage());

        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }
}