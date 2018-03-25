/*
 * PopularMovies by bytetonight
 * Created for the Udacity (c) Android (c) Developer Nanodegree
 * This software uses a remote API kindly made accessible by
 * https://www.themoviedb.org/
 *
 * Copyright (c) 2017.
 */

package android.example.com.foodoo.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.example.com.foodoo.R;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Created by ByteTonight on 05.06.2017.
 */

public class Utils {
    private static final String LOG_TAG = Utils.class.getName();
    private static final int READ_TIMEOUT_MS = 10000;
    private static final int CONNECT_TIMEOUT_MS = 15000;


    public static boolean isWifi(Context context)
    {
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isWiFi = activeNetwork.getType() == ConnectivityManager.TYPE_WIFI;
        return isWiFi;
    }

    public static boolean hasConnection(Context context)
    {
        final ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }

    /**
     * Store key,value pairs in Android Shared Preferences
     *
     * @param key   to store
     * @param value to store
     */
    public static void writeStringToPreferences(Context context, String key, String value) {

        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.apply();
        Log.v("writePreferences", key + " : " + value);
    }


    /**
     * Read key,value pairs from Android Shared Preferences
     *
     * @param key to read
     * @return
     */
    public static String readStringFromPreferences(Context context, String key) {
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        String returnData = sharedPref.getString(key, "0");
        //Let's see what we got from shared preferences
        Log.v("readPreferences", key + " = " + returnData);
        return returnData;
    }


}
