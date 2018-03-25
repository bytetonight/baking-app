package android.example.com.foodoo.widgets;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.example.com.foodoo.R;
import android.example.com.foodoo.models.Recipe;
import android.widget.RemoteViews;


public class IngredientWidgetProvider extends AppWidgetProvider {


    private static Recipe recipe;


    public static Recipe getRecipe() {
        return recipe;
    }

    /**
     * Called from {@link IngredientsWidgetUpdateService} which received the Recipe in the Bundle
     *
     * @param context
     * @param appWidgetManager
     * @param appWidgetIds
     * @param recipe           is the recipe sent to {@link IngredientsWidgetUpdateService as part of the Bundle} and from there to here
     */
    static void updateWidget(Context context, AppWidgetManager appWidgetManager,
                             int appWidgetIds[], Recipe recipe) {
        IngredientWidgetProvider.recipe = recipe;

        for (int appWidgetId : appWidgetIds) {
            // Calling the RemoteViewsFactory directly is an awesome source for unexplainable nothingness
            // ergo ... call the service that uses the factory to return a remoteviews object
            Intent intent = new Intent(context, ListViewWidgetRemoteViewsAdapterService.class);
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredients_list_widget_layout);

            views.setRemoteAdapter(R.id.appwidget_ingredients_listView, intent);
            ComponentName component = new ComponentName(context, IngredientWidgetProvider.class);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.appwidget_ingredients_listView);
            appWidgetManager.updateAppWidget(component, views);
        }

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        /*// There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateWidget(context, appWidgetManager, appWidgetId);
        }*/
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
    }
}

