package ua.tsisar.pavel.nazk.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import ua.tsisar.pavel.nazk.R;
import ua.tsisar.pavel.nazk.search.SearchFilters;

public class SearchFiltersActivity extends AppCompatActivity{

    private static final String EXTRA_QUERY = "query";
    private static final String EXTRA_DECLARATION_YEAR = "DeclarationYear";
    private static final String EXTRA_DECLARATION_TYPE = "DeclarationType";
    private static final String EXTRA_DOCUMENT_TYPE = "DocumentType";
    private static final String EXTRA_DT_START = "dtStart";
    private static final String EXTRA_DT_END = "dtEnd";

    private String query;
    private int declarationYear;
    private int declarationType;
    private int documentType;
    private String dtStart;
    private String dtEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_filters);

        Intent in = getIntent();
        query = in.getStringExtra(EXTRA_QUERY);
        declarationYear = in.getIntExtra(EXTRA_DECLARATION_YEAR, 0);
        declarationType = in.getIntExtra(EXTRA_DECLARATION_TYPE, SearchFilters.DECLARATION_ALL);
        documentType = in.getIntExtra(EXTRA_DOCUMENT_TYPE, SearchFilters.DOCUMENT_ALL);
        dtStart = in.getStringExtra(EXTRA_DT_START);
        dtEnd = in.getStringExtra(EXTRA_DT_END);

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
                Date date = new Date(null);

                String query = queryEditText.getText().toString();
                String year = declarationYearEditText.getText().toString();
                Intent out = new Intent();
                out.putExtra(EXTRA_QUERY, query);
                out.putExtra(EXTRA_DECLARATION_YEAR, year.length() == 0?0:Integer.valueOf(year));
                out.putExtra(EXTRA_DECLARATION_TYPE, declarationType);
                out.putExtra(EXTRA_DOCUMENT_TYPE, documentType);
                out.putExtra(EXTRA_DT_START, dtStart.length() == 0 && dtEnd.length() != 0 ?
                        "2000-01-01" : dtStart);
                out.putExtra(EXTRA_DT_END, dtEnd.length() == 0 && dtStart.length() != 0 ?
                        String.format(getString(R.string.date_format),
                                date.getYear(), date.getMonth()+1, date.getDay()) : dtEnd);
                setResult(RESULT_OK, out);
                finish();
            }
        );

//        dateButtonClick(new ButtonText(findViewById(R.id.dt_start_Button), dtStart));
//        dateButtonClick(new ButtonText(findViewById(R.id.dt_end_Button), dtEnd));

        Button btnStart = findViewById(R.id.dt_start_Button);
        btnStart.setOnClickListener((View view) -> {
            Date date = new Date(dtStart);
            DatePickerDialog datePickerDialog =
                    new DatePickerDialog(this, (datePicker, year, month, day) ->{
                        dtStart = String.format(getString(R.string.date_format),year, month+1, day);
                        btnStart.setText(dtStart);
                    }, date.getYear(), date.getMonth(), date.getDay());
            datePickerDialog.show();
        });
        if(dtStart.length() != 0)
            btnStart.setText(dtStart);

        Button btnEnd = findViewById(R.id.dt_end_Button);
        btnEnd.setOnClickListener((View view) -> {
            Date date = new Date(dtEnd);
            DatePickerDialog datePickerDialog =
                    new DatePickerDialog(this, (datePicker, year, month, day) ->{
                        dtEnd = String.format(getString(R.string.date_format),year, month+1, day);
                        btnEnd.setText(dtEnd);
                    }, date.getYear(), date.getMonth(), date.getDay());
            datePickerDialog.show();
        });
        if(dtEnd.length() != 0)
            btnEnd.setText(dtEnd);

    }

//    private void dateButtonClick(ButtonText buttonText){
//        buttonText.getButton().setOnClickListener((View view) -> {
//            Date date = new Date(buttonText.getDateStartEnd());
//            DatePickerDialog datePickerDialog =
//                    new DatePickerDialog(this, (datePicker, year, month, day) ->
//                            buttonText.setDateStartEnd(String.format(getString(R.string.date_format),
//                                    year, month+1, day)), date.getYear(), date.getMonth(), date.getDay());
//            datePickerDialog.show();
//        });
//        if(buttonText.dateStartEnd.length() != 0)
//            buttonText.getButton().setText(buttonText.dateStartEnd);
//    }

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

//    private class ButtonText {
//        private Button button;
//        private String dateStartEnd;
//
//        ButtonText(Button button, String text){
//            this.button = button;
//            this.dateStartEnd = text;
//        }
//
//        Button getButton(){
//            return button;
//        }
//
//        String getDateStartEnd(){
//            return dateStartEnd;
//        }
//
//        void setDateStartEnd(String text){
//            this.dateStartEnd = text;
//            this.button.setText(text);
//        }
//    }

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
