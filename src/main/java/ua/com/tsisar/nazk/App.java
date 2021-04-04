package ua.com.tsisar.nazk;

import android.app.Application;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import ua.com.tsisar.nazk.api.ApiInterface;
import ua.com.tsisar.nazk.filters.SearchFilters;
import ua.com.tsisar.nazk.filters.TestFilters;

public class App extends Application {
    private static ApiInterface apiInterface;
    private static SearchFilters searchFilters;
    private static TestFilters testFilters;

    @Override
    public void onCreate() {
        super.onCreate();

        searchFilters = new SearchFilters();
        testFilters = new TestFilters();

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

    public static TestFilters getTestFilters() {
        return testFilters;
    }
}
