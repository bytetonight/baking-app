package android.example.com.foodoo;

import android.example.com.foodoo.ui.IngredientsListActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by ByteTonight on 04.03.2018.
 */

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent{
    void inject(DaggerApplication application);
    void inject(IngredientsListActivity activity);

    /*@Component.Builder
    interface Builder {

        @BindsInstance
        Builder application(Application application);

        AppComponent build();
    }*/
}
