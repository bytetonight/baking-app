package android.example.com.foodoo;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by ByteTonight on 04.03.2018.
 */

@Module
public class AppModule {
    private final DaggerApplication application;

    public AppModule(DaggerApplication app) {
        application = app;
    }

    @Provides
    @Singleton
    Context providesApplicationContext() {
        return application;
    }


}
