package com.android.nitecafe.whirlpoolnews.dagger;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.android.nitecafe.whirlpoolnews.IWhirlpoolService;
import com.android.nitecafe.whirlpoolnews.NewsController;
import com.android.nitecafe.whirlpoolnews.WhirlpoolRestClient;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

/**
 * Created by Graham-i5 on 1/30/2016.
 */
@Module
public class DaggerModule {

    private String mApiKey;
    private String mBaseUrl;

    public DaggerModule(String apiKey, String baseUrl) {
        mApiKey = apiKey;
        mBaseUrl = baseUrl;
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient() {
        OkHttpClient client = new OkHttpClient();
        client.interceptors().add(chain -> {
            final HttpUrl build = chain.request().httpUrl().newBuilder()
                    .addQueryParameter("key", mApiKey)
                    .addQueryParameter("output", "json")
                    .build();

            final Request request = chain.request().newBuilder().url(build).build();
            return chain.proceed(request);
        });

        return client;
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(OkHttpClient client){
        return new Retrofit.Builder()
                .baseUrl(mBaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client)
                .build();
    }

    @Provides
    @Singleton
    IWhirlpoolService provideWhirlPoolService(Retrofit retrofit)
    {
        return retrofit.create(IWhirlpoolService.class);
    }

    @Provides
    @Singleton
        // Application reference must come from AppModule.class
    SharedPreferences providesSharedPreferences(Application application) {
        return PreferenceManager.getDefaultSharedPreferences(application);
    }
}