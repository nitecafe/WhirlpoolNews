package com.android.nitecafe.whirlpoolnews.dagger;

import com.android.nitecafe.whirlpoolnews.ui.NewsFragment;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, DaggerModule.class})
public interface DaggerComponent {
    void inject(NewsFragment activity);
}
