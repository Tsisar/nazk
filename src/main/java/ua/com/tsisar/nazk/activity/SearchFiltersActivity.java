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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import ua.com.tsisar.nazk.App;
import ua.com.tsisar.nazk.R;
import ua.com.tsisar.nazk.filters.DeclarationType;
import ua.com.tsisar.nazk.filters.DocumentType;
import ua.com.tsisar.nazk.util.Date;

public class SearchFiltersActivity extends AppCompatActivity {
    private static final String TAG = "MyLog";

    private EditText editTextQuery;
    private EditText editTextYear;
    private TextView textViewPeriod;

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

        startDate = new Date().set(App.getFilters().period().startDate());
        endDate = new Date().set(App.getFilters().period().endDate());

        initEditTextQuery();
        initEditTextYear();

        initSpinnerDocumentType();
        initSpinnerDeclarationType();

        textViewPeriod = findViewById(R.id.text_view_period);
        if(!startDate.isClear() && !endDate.isClear()){
            textViewPeriod.setText(String.format(getString(R.string.split_period),
                    startDate.toString(), endDate.toString()));
        }
    }

    private void initEditTextQuery(){
        editTextQuery = findViewById(R.id.edit_text_query);
        if(!App.getFilters().query().isClear()) {
            editTextQuery.setText(App.getFilters().query().get());
        }
    }

    private void initEditTextYear(){
        editTextYear = findViewById(R.id.edit_text_year);
        if(!App.getFilters().declarationYear().isClear()) {
            editTextYear.setText(String.valueOf(App.getFilters().declarationYear().get()));
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
                    int year = new Date().now().getYear();

                    int a = 2015/(int)Math.pow(10, 4-length);
                    int b = tryParse(editable.toString());
                    int c = year/(int)Math.pow(10, 4-length);

                    if(a > b || b > c){
                        editable.delete(length-1, length);
                        //TODO переробити на нормальний меседж
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
        if(App.getFilters().documentType().isClear()){
            spinner.setSelection(DocumentType.DOCUMENT_ALL);
        }else {
            spinner.setSelection(App.getFilters().documentType().get());
        }
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                App.getFilters().documentType().set(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                App.getFilters().documentType().set(DocumentType.DOCUMENT_ALL);
            }
        });
    }

    private void initSpinnerDeclarationType(){
        Spinner spinner = initSpinner(R.id.spinner_declaration_type, R.array.array_declaration_type);
        if(App.getFilters().declarationType().isClear()){
            spinner.setSelection(DeclarationType.DECLARATION_ALL);
        }else {
            spinner.setSelection(App.getFilters().declarationType().get());
        }
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                App.getFilters().declarationType().set(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                App.getFilters().declarationType().set(DeclarationType.DECLARATION_ALL);
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

    public void setStartDate(View view){
        Date date = startDate.isClear() ? new Date().now() : new Date().set(startDate);

        DatePickerDialog dialog = new DatePickerDialog(this, (datePicker, year, month, day) -> {
            startDate.set(year, month, day);
            setEndDate(view);
        }, date.getYear(), date.getMonth(), date.getDay());
        dialog.setOnCancelListener(dialog12 -> {
            startDate.clear();
            endDate.clear();
            textViewPeriod.setText(R.string.button_period);
        });
        dialog.getDatePicker().setMinDate(new Date().set(2016,7,1).toMillis());
        dialog.getDatePicker().setMaxDate(new Date().now().toMillis());
        dialog.setTitle(R.string.dialog_title_start_date);
        dialog.show();
    }

    public void setEndDate(View view){
        Date date = endDate.isClear() ? new Date().now() : new Date().set(endDate);

        DatePickerDialog dialog = new DatePickerDialog(this, (datePicker, year, month, day) -> {
            endDate.set(year, month, day);
            textViewPeriod.setText(String.format(getString(R.string.split_period),
                    startDate.toString(), endDate.toString()));
        }, date.getYear(), date.getMonth(), date.getDay());
        dialog.setOnCancelListener(dialog12 -> {
            startDate.clear();
            endDate.clear();
            textViewPeriod.setText(R.string.button_period);
        });
        dialog.getDatePicker().setMinDate(startDate.toMillis());
        dialog.getDatePicker().setMaxDate(new Date().now().toMillis());
        dialog.setTitle(R.string.dialog_title_end_date);
        dialog.show();
    }

    public void onFindClick(View view) {
        App.getFilters().query().set(editTextQuery.getText().toString());

        checkEditTextYear();
        App.getFilters().declarationYear().set(tryParse(editTextYear.getText().toString()));

//        if(startDate.isClear() && !endDate.isClear())
//            startDate.set(2016,7,1);
//        if(!startDate.isClear() && endDate.isClear())
//            endDate.now();
//
//        //if startDate > endDate reverse it
//        if(startDate.compareTo(endDate) > 0){
//            Date tmp = new Date().set(endDate);
//            endDate.set(startDate);
//            startDate.set(tmp);
//        }
//
//        if(endDate.compareTo(new Date().now()) > 0){
//            endDate.now();
//        }

        App.getFilters().period().startDate().set(startDate);
        App.getFilters().period().endDate().set(endDate);

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