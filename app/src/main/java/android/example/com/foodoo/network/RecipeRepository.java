package android.example.com.foodoo.network;

import android.content.Context;
import android.example.com.foodoo.R;
import android.example.com.foodoo.config.Config;
import android.example.com.foodoo.handlers.MessageEvent;
import android.example.com.foodoo.models.BaseModel;
import android.example.com.foodoo.models.Ingredient;
import android.example.com.foodoo.models.Recipe;
import android.example.com.foodoo.models.Step;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ByteTonight on 26.12.2017.
 */

public class RecipeRepository implements AbstractDataSource {

    private static final String LOG_TAG = RecipeRepository.class.getName();

    private static final String KEY_RECIPE_ID = "id";
    private static final String KEY_RECIPE_NAME = "name";
    private static final String KEY_RECIPE_INGREDIENTS = "ingredients";
    private static final String KEY_RECIPE_STEPS = "steps";
    private static final String KEY_RECIPE_SERVINGS = "servings";
    private static final String KEY_RECIPE_IMAGE = "image";

    private static final String KEY_INGREDIENT_QUANTITY = "quantity";
    private static final String KEY_INGREDIENT_MEASURE = "measure";
    private static final String KEY_INGREDIENT_INGREDIENT = "ingredient";

    private static final String KEY_STEP_ID = "id";
    private static final String KEY_STEP_SHORT_DESCRIPTION = "shortDescription";
    private static final String KEY_STEP_DESCRIPTION = "description";
    private static final String KEY_STEP_VIDEO_URL = "videoURL";
    private static final String KEY_STEP_THUMBNAIL_URL = "thumbnailURL";

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException exception) {
            Log.e(LOG_TAG, "Error with creating URL", exception);
            return null;
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        if (url == null)
            return jsonResponse;
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(Config.READ_TIMEOUT_MS /* milliseconds */);
            urlConnection.setConnectTimeout(Config.CONNECT_TIMEOUT_MS /* milliseconds */);
            urlConnection.connect();

            int responseCode = urlConnection.getResponseCode();
            switch (responseCode) {
                case 200:
                    inputStream = urlConnection.getInputStream();
                    jsonResponse = readFromStream(inputStream);
                    break;
                default:
                    Log.e(LOG_TAG, "Server responded with :" + responseCode);
                    //do something
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error trying to fetch JSON data", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // function must handle java.io.IOException here
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }



    private static List<Recipe> parseJSONtoRecipeList(Context context, String response) {

        if (response.isEmpty()) {
            return null;
        }

        List<Recipe> recipes = new ArrayList<>();

        try {
            JSONArray recipesArray = new JSONArray(response);

            //Uncomment the 2 lines below to raise a JSONException, and see how it's handled
            /*if (true)
                throw new JSONException("Some Element was not found... yada");*/

            for (int i = 0; i < recipesArray.length(); ++i) {
                JSONObject recipeObj = recipesArray.getJSONObject(i);
                int id = 0;
                String name = null;
                List<Ingredient> ingredients = new ArrayList<>();
                List<Step> steps = new ArrayList<>();
                int servings = 0;
                String image = "";

                if (recipeObj.has(KEY_RECIPE_ID)) {
                    id = recipeObj.getInt(KEY_RECIPE_ID);
                }

                if (recipeObj.has(KEY_RECIPE_NAME)) {
                    name = recipeObj.getString(KEY_RECIPE_NAME);
                }

                if (recipeObj.has(KEY_RECIPE_SERVINGS)) {
                    servings = recipeObj.getInt(KEY_RECIPE_SERVINGS);
                }

                if (recipeObj.has(KEY_RECIPE_IMAGE)) {
                    image = recipeObj.getString(KEY_RECIPE_IMAGE);
                }

                if (recipeObj.has(KEY_RECIPE_INGREDIENTS)) {

                    JSONArray ingredientsArray = recipeObj.getJSONArray(KEY_RECIPE_INGREDIENTS);

                    for (int iIng = 0; iIng < ingredientsArray.length(); ++iIng) {

                        JSONObject ingredientObj = ingredientsArray.getJSONObject(iIng);

                        double quantity = 0;
                        String measure = null;
                        String ingredient = null;

                        if (ingredientObj.has(KEY_INGREDIENT_QUANTITY)) {
                            quantity = ingredientObj.getDouble(KEY_INGREDIENT_QUANTITY);
                        }
                        if (ingredientObj.has(KEY_INGREDIENT_MEASURE)) {
                            measure = ingredientObj.getString(KEY_INGREDIENT_MEASURE);
                        }
                        if (ingredientObj.has(KEY_INGREDIENT_INGREDIENT)) {
                            ingredient = ingredientObj.getString(KEY_INGREDIENT_INGREDIENT);
                        }

                        Ingredient myIngredient = new Ingredient();
                        myIngredient.setQuantity(quantity);
                        myIngredient.setMeasure(measure);
                        myIngredient.setIngredient(ingredient);

                        ingredients.add(myIngredient);
                    }
                }

                if (recipeObj.has(KEY_RECIPE_STEPS)) {

                    JSONArray stepsArray = recipeObj.getJSONArray(KEY_RECIPE_STEPS);

                    for (int iStep = 0; iStep < stepsArray.length(); ++iStep) {

                        JSONObject stepObj = stepsArray.getJSONObject(iStep);

                        int stepId = 0;
                        String shortDescription = null;
                        String description = null;
                        String videoURL = null;
                        String thumbnailURL = null;

                        if (stepObj.has(KEY_STEP_ID)) {
                            stepId = stepObj.getInt(KEY_STEP_ID);
                        }
                        if (stepObj.has(KEY_STEP_SHORT_DESCRIPTION)) {
                            shortDescription = stepObj.getString(KEY_STEP_SHORT_DESCRIPTION);
                        }
                        if (stepObj.has(KEY_STEP_DESCRIPTION)) {
                            description = stepObj.getString(KEY_STEP_DESCRIPTION);
                        }
                        if (stepObj.has(KEY_STEP_VIDEO_URL)) {
                            videoURL = stepObj.getString(KEY_STEP_VIDEO_URL);
                        }
                        if (stepObj.has(KEY_STEP_THUMBNAIL_URL)) {
                            thumbnailURL = stepObj.getString(KEY_STEP_THUMBNAIL_URL);
                        }

                        Step step = new Step();
                        step.setId(stepId);
                        step.setShortDescription(shortDescription);
                        step.setDescription(description);
                        step.setVideoURL(videoURL);
                        step.setThumbnailURL(thumbnailURL);

                        steps.add(step);
                    }
                }

                Recipe recipe = new Recipe();
                recipe.setId(id);
                recipe.setName(name);
                recipe.setServings(servings);
                recipe.setImage(image);
                recipe.setIngredients(ingredients);
                recipe.setSteps(steps);
                recipes.add(recipe);
            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, context.getString(R.string.json_error), e);
            EventBus.getDefault().post(new
                    MessageEvent(Config.EVENTBUS_MESSAGE_TYPE_EXCEPTION, context.getString(R.string.json_error)+
                    "\n"+e.getMessage()));
        }
        return recipes;
    }

    public static List<? extends BaseModel> loadRecipes(Context context) {
        String jsonResponse = null;
        URL currentUrl = createUrl(Config.API_JSON_SOURCE);
        try {
            jsonResponse = makeHttpRequest(currentUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return parseJSONtoRecipeList(context, jsonResponse);
    }
}
