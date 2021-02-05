package zingmyorder.kitchen.mobile.di;

import zingmyorder.kitchen.mobile.persistance.AppPreferencesHelper;

import java.net.CookieManager;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Network Module which provides the retrofit Builder
 */
@Module
public class NetworkModule {

    private static final long CONNECTION_TIMEOUT = 30000;

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(HttpLoggingInterceptor logging,final AppPreferencesHelper appPreferencesHelper) {
        CookieManager cookieHandler = new CookieManager();
//        cookieHandler.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder().
                readTimeout(CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS).
                connectTimeout(CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS).
                writeTimeout(CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS).
                cookieJar(new JavaNetCookieJar(cookieHandler));
        httpClient.addNetworkInterceptor(
                chain -> {
                    Request originalRequest = chain.request();
                    Request.Builder builder = originalRequest.newBuilder();

//                    builder.header("Accept: application/json")
                    Request authorisedRequest = builder.build();
                    Response originalResponse = chain.proceed(authorisedRequest);

                    return originalResponse;
                });
//        httpClient.addInterceptor(new CryptoInterceptor());
        httpClient.addInterceptor(logging);
        return httpClient.build();

    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(OkHttpClient okHttpClient) {
//        Gson gson = new GsonBuilder()
//                .setLenient()
//                .create();
        return new Retrofit.Builder()
                .baseUrl("https://zingmyorder.com/")
//                .baseUrl("https://dev.zingmyorder.com/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
    }


    @Provides
    @Singleton
    HttpLoggingInterceptor provideLogging() {
        return new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
    }

}
