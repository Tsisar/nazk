package ua.com.tsisar.nazk.api;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;
import ua.com.tsisar.nazk.dto.AnswerDTO;

/**
 query – поле "Пошуковий запит", текстове, від 3 до 255 символів. При невідповідності довжини
 повертає помилку JSON {error: 1310101}. При існуванні цього поля сортування буде йти по
 релевантності, а не по даті подання;

 user_declarant_id – поле "ID суб’єкта декларування", числове, від 1 до 10000000.
 При невідповідності заповнення повертає помилку JSON {error: 1310111} або JSON {error: 1310112};

 document_type – поле "Тип документа", числове, від 1 до 3 (1 - декларація; 2 - повідомлення про
 суттєві зміни в майновому стані; 3 - виправлена декларація; 4 - виправлене повідомлення про
 суттєві зміни в майновому стані). При невідповідності заповнення повертає помилку JSON {error:
 1310121} або JSON {error: 1310122};

 declaration_type – поле "Тип декларації", числове, від 1 до 4 (1 - щорічна; 2 - перед звільненням;
 3 - після звільнення; 4 - кандидата на посаду). При невідповідності заповнення повертає помилку
 JSON {error: 1310131} або JSON {error: 1310132};

 declaration_year – поле "Рік декларації", числове, від 2015 до поточного року. При невідповідності
 заповнення повертає помилку JSON {error: 1310141} або JSON {error: 1310142};

 start_date – поле "Початок дати подання", числове, від 1470009600 (01.08.2016) до поточної дати у
 секундах від початку епохи UNIX. При невідповідності заповнення повертає помилку JSON {error:
 1310151} або JSON {error: 1310152};

 end_date – поле "Кінець дати подання", числове, від 1470009600 (01.08.2016) до поточної дати у
 секундах від початку епохи UNIX. При невідповідності заповнення повертає помилку JSON {error:
 1310161} або JSON {error: 1310162};

 page – поле "№ сторінки" для пересування по сторінках, якщо кількість результатів перевищує 100,
 числове, від 1 до 100. При невідповідності заповнення повертає помилку JSON {error: 1310171} або
 JSON {error: 1310172}.*/

public interface ApiInterface {
    String URL_BASE = "https://public-api.nazk.gov.ua/v2/documents/";

    @Headers("Content-Type: application/json")
    @GET("list")
    Single<AnswerDTO> searchDeclarations(@Query("query") String query,
                                         @Query("user_declarant_id") String userDeclarantId,
                                         @Query("document_type") String documentType,
                                         @Query("declaration_type") String declarationType,
                                         @Query("declaration_year") String declarationYear,
                                         @Query("start_date") String startDate,
                                         @Query("end_date") String endDate,
                                         @Query("page") String Page);
}