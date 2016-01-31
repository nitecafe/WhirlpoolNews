package com.android.nitecafe.whirlpoolnews;

import android.app.Application;

import com.android.nitecafe.whirlpoolnews.constants.StringConstants;
import com.android.nitecafe.whirlpoolnews.dagger.AppModule;
import com.android.nitecafe.whirlpoolnews.dagger.DaggerComponent;
import com.android.nitecafe.whirlpoolnews.dagger.DaggerDaggerComponent;
import com.android.nitecafe.whirlpoolnews.dagger.DaggerModule;
import com.squareup.leakcanary.LeakCanary;

/**
 * Created by Graham-i5 on 1/30/2016.
 */
public class WhirlpoolApp extends Application {


    private DaggerComponent daggerComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        daggerComponent = DaggerDaggerComponent.builder()
                .appModule(new AppModule(this))
                .daggerModule(new DaggerModule(BuildConfig.WHIRLPOOL_API_KEY, StringConstants.WHIRLPOOL_API_URL))
                .build();

//        LeakCanary.install(this);  //not working on android 6.0
    }

    public DaggerComponent getDaggerComponent() {
        return daggerComponent;
    }
}
