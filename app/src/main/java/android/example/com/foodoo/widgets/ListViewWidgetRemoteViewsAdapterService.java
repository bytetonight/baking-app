package android.example.com.foodoo.widgets;

/*
* Copyright (C) 2017 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*  	http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

import android.content.Context;
import android.content.Intent;
import android.example.com.foodoo.R;
import android.example.com.foodoo.models.Ingredient;
import android.example.com.foodoo.models.Recipe;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

/**
 * This service acts as an Adapter for  {@link Recipe#getIngredients()}
 * and is only for the RemoteViews factory to populate ListViews
 */
public class ListViewWidgetRemoteViewsAdapterService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListViewRemoteViewsFactory(this.getApplicationContext());
    }
}

class ListViewRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context context;
    private Recipe recipe;

    ListViewRemoteViewsFactory(Context applicationContext) {
        context = applicationContext;
    }

    @Override
    public void onCreate() {

    }

    //called on start and when notifyAppWidgetViewDataChanged is called
    @Override
    public void onDataSetChanged() {
        recipe = IngredientWidgetProvider.getRecipe();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return recipe.getIngredients() == null ? 0 : recipe.getIngredients().size();
    }

    /**
     * This method acts like the onBindViewHolder method in an Adapter
     *
     * @param position The current position of the item in the GridView to be displayed
     * @return The RemoteViews object to display for the provided position
     */
    @Override
    public RemoteViews getViewAt(int position) {
        final Ingredient ingredient = recipe.getIngredients().get(position);
        // If you don't source in your list ITEM layout, don't be surprised if nothing shows,
        // not even an error. That cost me 2 hours
        RemoteViews views = new RemoteViews(context.getPackageName(),
                R.layout.ingredient_widget_list_item);

        views.setInt(R.id.relativeLayout, "setBackgroundColor",  ingredient.getCardColor());
        views.setTextViewText(R.id.textViewQuantity, ingredient.getQuantityString());
        views.setTextViewText(R.id.textViewMeasure, ingredient.getMeasure());
        views.setTextViewText(R.id.textViewIngredient, ingredient.getIngredient());
        // Works on my phone with API 24, on Emulator API 25,
        // but on the Emulator with API 19 ... no icons showing
        // however in the regular recyclerviews the icons show on all devices
        views.setImageViewResource(R.id.ingredient_unit_icon, ingredient.getMeasure_image_resource());

        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1; // Treat all items in the GridView the same
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}

