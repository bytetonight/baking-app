package android.example.com.foodoo.widgets;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.example.com.foodoo.config.Config;
import android.example.com.foodoo.models.Recipe;
import android.example.com.foodoo.ui.IngredientsListActivity;
import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by ByteTonight on 05.03.2018.
 * Criteria : Application has a companion homescreen widget.
 *            Widget displays ingredient list for desired recipe.
 * Ok
 */

public class IngredientsWidgetUpdateService extends IntentService {

    public static final String ACTION_UPDATE_INGREDIENTS_WIDGET =
            "android.example.com.foodoo.action.updateDuhWidget";



    public IngredientsWidgetUpdateService() {
        super("IngredientsWidgetUpdateService");
    }

    /**
     * Async Handler
     * triggered in {@link IngredientsListActivity#startIngredientsWidgetUpdateService()}
     * @param intent includes the action to be performed, and the data to perform on/with
     */
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            String action = intent.getAction();
            if (action != null) {
                if (action.equals(ACTION_UPDATE_INGREDIENTS_WIDGET)) {
                    Bundle bundle = intent.getBundleExtra(Config.DATA_KEY_RECIPE_BUNDLE);
                    Recipe recipe = bundle.getParcelable(Config.DATA_KEY_RECIPE);

                    AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
                    int[] appWidgetIds = appWidgetManager.getAppWidgetIds(
                            new ComponentName(this, IngredientWidgetProvider.class));

                    IngredientWidgetProvider.updateWidget(this, appWidgetManager,
                            appWidgetIds, recipe);
                }
            }
        }
    }
}
