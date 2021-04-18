package ua.tsisar.pavel.nazk;

import android.app.Application;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import ua.tsisar.pavel.nazk.api.ApiInterface;
import ua.tsisar.pavel.nazk.filters.SearchFilters;

public class App extends Application {
    private static ApiInterface apiInterface;
    private static SearchFilters searchFilters;

    @Override
    public void onCreate() {
        super.onCreate();

        searchFilters = new SearchFilters();

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        // add your other interceptors …

        // add logging as last interceptor
        httpClient.addInterceptor(logging);  // <-- this is the important line!

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiInterface.URL_BASE)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();
        apiInterface = retrofit.create(ApiInterface.class); //Создаем объект, при помощи которого будем выполнять запросы

    }

    public static ApiInterface getApi() {
        return apiInterface;
    }

    public static SearchFilters getFilters() {
        return searchFilters;
    }
}