package android.example.com.foodoo;

import android.app.Application;

/**
 * Created by ByteTonight on 04.03.2018.
 */

public class DaggerApplication extends Application {
    AppComponent appComponent;

    @Override public void onCreate() {
        super.onCreate();

        // See https://stackoverflow.com/questions/36521302/dagger-2-2-component-builder-module-method-deprecated/36521366#36521366
        // about ignoring deprecation notice of appModule
        appComponent = DaggerAppComponent.builder().appModule(new AppModule(this)).build();

        appComponent.inject(this);
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}
