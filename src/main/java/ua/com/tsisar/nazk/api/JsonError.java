package ua.com.tsisar.nazk.api;

import java.util.HashMap;
import java.util.Map;

public enum JsonError {
    //TODO Перевірити чи цифри 131011[1] 131011[2] відповідають нижнй/верхній границі значень, поправити меседжі
    // Перенести стрінги в ресурси
    QUERY_LONG_ERROR(1310101, "Пошуковий запит має містити мінимум 2 символи."),
    USER_DECLARANT_ID_ERROR_1(1310111, "Невідповідність заповнення значення поля" +
            "\"ID суб’єкта декларування\". Внесіть числове значення, від 1 до 10000000."),
    USER_DECLARANT_ID_ERROR_2(1310112, "Невідповідність заповнення значення поля" +
            "\"ID суб’єкта декларування\". Внесіть числове значення, від 1 до 10000000."),
    DOCUMENT_TYPE_ERROR_1(1310121, "Невідповідність заповнення поля " +
            "\"Тип документа\"."),
    DOCUMENT_TYPE_ERROR_2(1310122, "Невідповідність заповнення поля " +
            "\"Тип документа\"."),
    DECLARATION_TYPE_ERROR_1(1310131, "Невідповідність заповнення поля " +
            "\"Тип декларації\"."),
    DECLARATION_TYPE_ERROR_2(1310132, "Невідповідність заповнення поля " +
            "\"Тип декларації\"."),
    DECLARATION_YEAR_ERROR_1(1310141, "Невідповідність заповнення поля " +
            "\"Рік декларації\". Внесіть рік починаючи з 2015 до поточного року."),
    DECLARATION_YEAR_ERROR_2(1310142, "Невідповідність заповнення поля " +
            "\"Рік декларації\". Внесіть рік починаючи з 2015 до поточного року."),
    START_DATE_ERROR_1(1310151, "Невідповідність заповнення поля " +
            "\"Початок дати подання\". Внесіть дату починаючи з (01.08.2016) до поточної дати."),
    START_DATE_ERROR_2(1310152, "Невідповідність заповнення поля " +
            "\"Початок дати подання\". Внесіть дату починаючи з (01.08.2016) до поточної дати."),
    END_DATE_ERROR_1(1310161, "Невідповідність заповнення поля " +
            "\"Кінець дати подання\". Внесіть дату починаючи з (01.08.2016) до поточної дати."),
    END_DATE_ERROR_2(1310162, "Невідповідність заповнення поля " +
            "\"Кінець дати подання\". Внесіть дату починаючи з (01.08.2016) до поточної дати."),
    PAGE_ERROR_1(1310171, "Невідповідність заповнення поля " +
            "\"№ сторінки\". Внесіть номер сторі в діапазоні від 1 до 100."),
    PAGE_ERROR_2(1310172, "Невідповідність заповнення поля " +
            "\"№ сторінки\". Внесіть номер сторі в діапазоні від 1 до 100.");
    private final Integer code;
    private final String message;

    JsonError(Integer code, String message){
        this.code = code;
        this.message = message;
    }

    public static JsonError get(Integer code) {
        return lookup.get(code);
    }

    private static final Map<Integer, JsonError> lookup = new HashMap<>();

    static {
        for (JsonError h : JsonError.values()) {
            lookup.put(h.getCode(), h);
        }
    }

    public Integer getCode(){
        return code;
    }

    public String getMessage() {
        return message;
    }
}