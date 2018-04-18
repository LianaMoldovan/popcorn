package ro.neusoft.lianamoldovan.moviedatabase.rest;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ro.neusoft.lianamoldovan.moviedatabase.BuildConfig;

/**
 * Created by liana.moldovan on 16.04.2018.
 */

public class RestService {
    private static Retrofit retrofit = null;

    private static OkHttpClient buildClient() {
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            // set the desired log level
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            // add logging as last interceptor
            httpClientBuilder.addInterceptor(logging);
        }

        return httpClientBuilder.build();
    }

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .client(buildClient())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(UrlConstants.BASE_URL)
                    .build();
        }
        return retrofit;
    }
}
