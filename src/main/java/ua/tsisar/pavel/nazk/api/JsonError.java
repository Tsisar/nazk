package ua.tsisar.pavel.nazk.api;

import java.util.HashMap;
import java.util.Map;

import ua.tsisar.pavel.nazk.R;

public enum JsonError {
    //TODO Перевірити чи цифри 131011[1] 131011[2] відповідають нижнй/верхній границі значень, поправити меседжі
    QUERY_LONG_ERROR(1310101, R.string.query_long_error),
    USER_DECLARANT_ID_ERROR_1(1310111, R.string.user_declarant_id_error),
    USER_DECLARANT_ID_ERROR_2(1310112, R.string.user_declarant_id_error),
    DOCUMENT_TYPE_ERROR_1(1310121, R.string.document_type_error),
    DOCUMENT_TYPE_ERROR_2(1310122, R.string.document_type_error),
    DECLARATION_TYPE_ERROR_1(1310131, R.string.declaration_type_error),
    DECLARATION_TYPE_ERROR_2(1310132, R.string.declaration_type_error),
    DECLARATION_YEAR_ERROR_1(1310141, R.string.declaration_year_error),
    DECLARATION_YEAR_ERROR_2(1310142, R.string.declaration_year_error),
    START_DATE_ERROR_1(1310151, R.string.start_date_error),
    START_DATE_ERROR_2(1310152, R.string.start_date_error),
    END_DATE_ERROR_1(1310161, R.string.end_date_error),
    END_DATE_ERROR_2(1310162, R.string.end_date_error),
    PAGE_ERROR_1(1310171, R.string.page_error),
    PAGE_ERROR_2(1310172, R.string.page_error);
    private final Integer code;
    private final Integer message;

    private static final Map<Integer, JsonError> lookup = new HashMap<>();

    static {
        for (JsonError h : JsonError.values()) {
            lookup.put(h.getCode(), h);
        }
    }

    JsonError(Integer code, Integer message) {
        this.code = code;
        this.message = message;
    }

    public static JsonError get(Integer code) {
        return lookup.get(code);
    }

    public Integer getCode() {
        return code;
    }

    public Integer getMessage() {
        return message;
    }
}