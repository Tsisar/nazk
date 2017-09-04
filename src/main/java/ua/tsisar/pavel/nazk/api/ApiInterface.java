package ua.tsisar.pavel.nazk.api;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;
import ua.tsisar.pavel.nazk.dto.AnswerDTO;


public interface ApiInterface {
    String URL_BASE = "https://public-api.nazk.gov.ua/v1/";

    @Headers("Content-Type: application/json")
    @GET("declaration/")
    Single<AnswerDTO> searchDeclarations(@Query("q") String query,                                         // пошуковий запит
                                         @Query("declarationYear") int declarationYear,                    // declarationYear: рік
                                         @Query("declarationType") int declarationType,                    // declarationType: 1 - щорічна; 2 - перед звільненням; 3 - після звільнення; 4 - кандидата на посаду;
                                         @Query("documentType") int documentType);                         // documentType: 1 - декларація; 2 - повідомлення про суттєві зміни в майновому стані; 3 - виправлена декларація; 4 - виправлене повідомлення про суттєві зміни в майновому стані;
                                         // @Query("positionTypes") List<Integer> positionTypes,           // positionTypes[]
                                         // @Query("positionCategories") String positionCategories,        // positionCategories[]
                                         // @Query("dateStartEnd") String dateStartEnd,                    // dateStartEnd
                                         // @Query("isRisk") boolean isRisk,                               // isRisk
                                         // @Query("responsiblePositions") String responsiblePositions);   // responsiblePositions[]
}