/*
 * PopularMovies by bytetonight
 * Created for the Udacity (c) Android (c) Developer Nanodegree
 * This software uses a remote API kindly made accessible by
 * https://www.themoviedb.org/
 *
 * Copyright (c) 2017.
 */

package android.example.com.foodoo.handlers;


import android.example.com.foodoo.models.Step;
import android.view.View;


/**
 * Created by ByteTonight on 12.11.2017.
 */

public interface ClickActionCallback {
    void stepOnClick(View v, Step step);
    void ingredientsButtonOnClick(View v);
}
