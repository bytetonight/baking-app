/*
 * PopularMovies by bytetonight
 * Created for the Udacity (c) Android (c) Developer Nanodegree
 * This software uses a remote API kindly made accessible by
 * https://www.themoviedb.org/
 *
 * Copyright (c) 2017.
 */

package android.example.com.foodoo.config;

/**
 * Created by ByteTonight on 28.10.2017.
 */

public class Config {
    private static final String API_ENDPOINT = "https://d17h27t6h515a5.cloudfront.net/";
    public static final String API_JSON_SOURCE = API_ENDPOINT + "topher/2017/May/59121517_baking/baking.json";
    public static final String NOTIFICATION_COMPAT_CHANNEL_ID = "mooh";
    public static final String DATA_KEY_RECIPE_LIST = "recipes";
    public static final String DATA_KEY_RECIPE = "recipe";
    public static final String DATA_KEY_STEP = "step";
    public static final String DATA_KEY_RECIPE_BUNDLE = "recipe_bundle";
    public static final String DATA_KEY_INGREDIENTS = "ingredients";

    public static final int RECYCLERVIEW_COLUMNS_PORTRAIT = 2;
    public static final int RECYCLERVIEW_COLUMNS_LAND = 3;
    public static final int READ_TIMEOUT_MS = 10000;
    public static final int CONNECT_TIMEOUT_MS = 15000;

    public static final String EVENTBUS_MESSAGE_TYPE_EXCEPTION = "exception";
    public static final String DATA_KEY_STEP_INDEX = "index";
    public static final String DATA_KEY_STEP_LIST = "stepListLength";
    public static final String DATA_KEY_LAST_STEP_INDEX = "lastIndex";
}
