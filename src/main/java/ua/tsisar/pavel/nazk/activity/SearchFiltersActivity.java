package ua.tsisar.pavel.nazk.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import ua.tsisar.pavel.nazk.App;
import ua.tsisar.pavel.nazk.R;
import ua.tsisar.pavel.nazk.filters.DeclarationType;
import ua.tsisar.pavel.nazk.filters.DocumentType;
import ua.tsisar.pavel.nazk.util.Date;

public class SearchFiltersActivity extends AppCompatActivity {

    private TextInputLayout inputTextQuery;
    private TextInputLayout inputTextYear;
    private TextInputEditText editTextQuery;
    private TextInputEditText editTextYear;
    private TextView textViewPeriod;

    private Date startDate;
    private Date endDate;

    private TextInputLayout inputTextDeclaration;
    private MaterialAutoCompleteTextView textViewDeclarationType;

    @DocumentType.Document
    private int documentType = DocumentType.DOCUMENT_ALL;

    @DeclarationType.Declaration
    private int declarationType = DeclarationType.DECLARATION_ALL;

    private static Integer tryParse(String string) {
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

        initSpinnerDeclarationType();
        initSpinnerDocumentType();

        textViewPeriod = findViewById(R.id.text_view_period);
        if (!startDate.isClear() && !endDate.isClear()) {
            textViewPeriod.setText(String.format(getString(R.string.split_period),
                    startDate.toString(), endDate.toString()));
        }
    }

    private void initEditTextQuery() {
        inputTextQuery = findViewById(R.id.input_text_query);
        editTextQuery = findViewById(R.id.edit_text_query);
        if (!App.getFilters().query().isClean()) {
            editTextQuery.setText(App.getFilters().query().get());
        }
        editTextQuery.setOnFocusChangeListener((view, hasFocus) -> {
            if (!hasFocus) {
                if (editTextQuery.getText() != null &&
                        editTextQuery.getText().toString().length() != 0 &&
                        (editTextQuery.getText().toString().length() < 3 ||
                                editTextQuery.getText().toString().length() > 255)) {
                    inputTextQuery.setError(getString(R.string.query_input_error));
                } else {
                    inputTextQuery.setError(null);
                }
            }
        });
        editTextQuery.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().length() > 3 && charSequence.toString().length() < 255) {
                    inputTextQuery.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    private void initEditTextYear() {
        inputTextYear = findViewById(R.id.input_text_year);
        editTextYear = findViewById(R.id.edit_text_year);
        if (!App.getFilters().declarationYear().isClean()) {
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

                if (length != 0) {
                    int year = new Date().now().getYear();

                    int a = 2015 / (int) Math.pow(10, 4 - length);
                    int b = tryParse(editable.toString());
                    int c = year / (int) Math.pow(10, 4 - length);

                    if (a > b || b > c) {
                        editable.delete(length - 1, length);
                        inputTextYear.setError(String.format(getString(R.string.year_input_error), year));
                    } else {
                        inputTextYear.setError(null);
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

    private void checkEditTextYear() {
        if (editTextYear.getText() != null &&
                !editTextYear.getText().toString().isEmpty() &&
                tryParse(editTextYear.getText().toString()) < 2015) {
            String actualYear = new Date().now().getYear().toString();
            editTextYear.setText(actualYear);
        }
    }

    private void initSpinnerDocumentType() {
        if (!App.getFilters().documentType().isClean()) {
            documentType = App.getFilters().documentType().get();
        }
        setEnableDeclarationTypeSpinner();

        String[] strings = getResources().getStringArray(R.array.array_document_type);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.item_spinner, strings);
        MaterialAutoCompleteTextView textViewDocumentType = findViewById(R.id.auto_complete_text_view_document_type);
        textViewDocumentType.setAdapter(adapter);
        textViewDocumentType.setText(documentType == DocumentType.DOCUMENT_ALL ? null : strings[documentType], false);
        textViewDocumentType.setOnItemClickListener((parent, view, position, id) -> {
            documentType = position;
            setEnableDeclarationTypeSpinner();
        });
    }

    private void initSpinnerDeclarationType() {
        if (!App.getFilters().declarationType().isClean()) {
            declarationType = App.getFilters().declarationType().get();
        }

        inputTextDeclaration = findViewById(R.id.input_text_declaration_type);
        String[] strings = getResources().getStringArray(R.array.array_declaration_type);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.item_spinner, strings);
        textViewDeclarationType = findViewById(R.id.auto_complete_text_view_declaration_type);
        textViewDeclarationType.setAdapter(adapter);
        textViewDeclarationType.setText(declarationType == DeclarationType.DECLARATION_ALL ? null : strings[declarationType], false);
        textViewDeclarationType.setOnItemClickListener((parent, view, position, id) -> declarationType = position);
    }

    private void setEnableDeclarationTypeSpinner() {
        if (documentType == DocumentType.DOCUMENT_DECLARATION ||
                documentType == DocumentType.DOCUMENT_NEW_DECLARATION) {
            inputTextDeclaration.setEnabled(true);
        } else {
            declarationType = DeclarationType.DECLARATION_ALL;
            inputTextDeclaration.setEnabled(false);
            textViewDeclarationType.setText(null, false);
        }
    }

    public void setStartDate(View view) {
        Date date = startDate.isClear() ? new Date().now() : new Date().set(startDate);

        DatePickerDialog dialog = new DatePickerDialog(this, (datePicker, year, month, day) -> {
            startDate.set(year, month, day);
            setEndDate();
        }, date.getYear(), date.getMonth(), date.getDay());
        dialog.setOnCancelListener(dialog12 -> {
            startDate.clear();
            endDate.clear();
            textViewPeriod.setText(null);
        });
        dialog.getDatePicker().setMinDate(new Date().set(2016, 7, 1).toMillis());
        dialog.getDatePicker().setMaxDate(new Date().now().toMillis());
        dialog.setTitle(R.string.dialog_title_start_date);
        dialog.show();
    }

    private void setEndDate() {
        Date date = endDate.isClear() ? new Date().now() : new Date().set(endDate);

        DatePickerDialog dialog = new DatePickerDialog(this, (datePicker, year, month, day) -> {
            endDate.set(year, month, day);
            textViewPeriod.setText(String.format(getString(R.string.split_period),
                    startDate.toString(), endDate.toString()));
        }, date.getYear(), date.getMonth(), date.getDay());
        dialog.setOnCancelListener(dialog12 -> {
            startDate.clear();
            endDate.clear();
            textViewPeriod.setText(null);
        });
        dialog.getDatePicker().setMinDate(startDate.toMillis());
        dialog.getDatePicker().setMaxDate(new Date().now().toMillis());
        dialog.setTitle(R.string.dialog_title_end_date);
        dialog.show();
    }

    public void onFindClick(View view) {
        checkEditTextYear();

        App.getFilters().page().clean();
        App.getFilters().query().set(editTextQuery.getText() != null ? editTextQuery.getText().toString() : "");
        App.getFilters().declarationYear().set(tryParse(editTextYear.getText() != null ? editTextYear.getText().toString() : ""));
        App.getFilters().documentType().set(documentType);
        App.getFilters().declarationType().set(declarationType);
        App.getFilters().period().startDate().set(startDate);
        App.getFilters().period().endDate().set(endDate);

        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }
}