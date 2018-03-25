/*
 * PopularMovies by bytetonight
 * Created for the Udacity (c) Android (c) Developer Nanodegree
 * This software uses a remote API kindly made accessible by
 * https://www.themoviedb.org/
 *
 * Copyright (c) 2017.
 */

package android.example.com.foodoo.handlers;


import android.content.Context;
import android.content.Intent;
import android.example.com.foodoo.config.Config;
import android.example.com.foodoo.models.Recipe;
import android.example.com.foodoo.models.Step;
import android.example.com.foodoo.ui.StepListActivity;
import android.view.View;
import android.widget.Toast;

public class DataBindingClickHandler {

    public void recipeOnClick(View v, Recipe recipe) {
        Context context = v.getContext();
        Intent recipeDetailsIntent = new Intent(context, StepListActivity.class);
        recipeDetailsIntent.putExtra(Config.DATA_KEY_RECIPE, recipe);
        context.startActivity(recipeDetailsIntent);
    }

}
