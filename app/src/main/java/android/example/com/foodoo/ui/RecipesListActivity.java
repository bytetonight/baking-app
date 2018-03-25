package android.example.com.foodoo.ui;

import android.content.Context;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.example.com.foodoo.R;
import android.example.com.foodoo.adapters.RecipeAdapter;
import android.example.com.foodoo.config.Config;
import android.example.com.foodoo.databinding.ActivityRecipesListBinding;
import android.example.com.foodoo.handlers.MessageEvent;
import android.example.com.foodoo.loaders.RecipeLoader;
import android.example.com.foodoo.models.BaseModel;
import android.example.com.foodoo.network.NetworkIdlingResource;
import android.example.com.foodoo.utils.ColorTools;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class RecipesListActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<? extends BaseModel>> {

    private NetworkIdlingResource networkNetworkIdlingResource;
    private static int RECIPES_LOADER_ID = 1;
    private List<? extends BaseModel> recipes;
    private RecipeAdapter recipeAdapter;
    private ActivityRecipesListBinding binding;


    @VisibleForTesting
    @NonNull
    public NetworkIdlingResource getNetworkNetworkIdlingResource() {
        if (networkNetworkIdlingResource == null)
        {
            networkNetworkIdlingResource = new NetworkIdlingResource();
        }
        return networkNetworkIdlingResource;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_recipes_list);
        //binding.setActivityHandledClick(this);
        if (savedInstanceState == null || recipes == null) {
            loadRecipes();
        } else {
            recipes = savedInstanceState.getParcelableArrayList(Config.DATA_KEY_RECIPE_LIST);
        }

        prepareLayout();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(Config.DATA_KEY_RECIPE_LIST,
                (ArrayList<? extends Parcelable>) recipes);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        recipes = savedInstanceState.getParcelableArrayList(Config.DATA_KEY_RECIPE_LIST);
    }

    @Override
    public Loader<List<? extends BaseModel>> onCreateLoader(int id, Bundle args) {
        return new RecipeLoader(RecipesListActivity.this);
    }


    @Override
    public void onLoadFinished(Loader<List<? extends BaseModel>> loader, List<? extends BaseModel> data) {
        if (data != null) {
            recipes = ColorTools.colorizeList(data, getResources().getColor(R.color.startColor),
                    getResources().getColor(R.color.endColor));
            recipes = data;
            recipeAdapter.setRecipes(data);

            // The IdlingResource is null in production.
            if (networkNetworkIdlingResource != null) {
                networkNetworkIdlingResource.setIdleState(true);
            }
        }
    }


    @Override
    public void onLoaderReset(Loader<List<? extends BaseModel>> loader) {

    }

    private void prepareLayout() {
        int columnCount = 2;
        switch (getResources().getConfiguration().orientation) {
            case Configuration.ORIENTATION_PORTRAIT:
                columnCount = Config.RECYCLERVIEW_COLUMNS_PORTRAIT;
                break;

            case Configuration.ORIENTATION_LANDSCAPE:
                columnCount = Config.RECYCLERVIEW_COLUMNS_LAND;
                break;
        }
        RecyclerView recipesListView = findViewById(R.id.recipes_list_recycler_view);
        GridLayoutManager layoutManager = new GridLayoutManager(RecipesListActivity.this, columnCount);
        recipesListView.setLayoutManager(layoutManager);
        recipesListView.setHasFixedSize(true);
        recipeAdapter = new RecipeAdapter(recipes);
        recipesListView.setAdapter(recipeAdapter);
    }

    private void loadRecipes() {
        // for Espresso Tests only
        if (networkNetworkIdlingResource != null) {
            networkNetworkIdlingResource.setIdleState(false);
        }

        LoaderManager loaderManager = getSupportLoaderManager();
        loaderManager.initLoader(RECIPES_LOADER_ID, null, RecipesListActivity.this);
    }


    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    /**
     * This method will be called when a MessageEvent is posted (in the UI thread for Toast)
     * mainly when a {@link org.json.JSONException} is thrown in
     * {@link android.example.com.foodoo.network.RecipeRepository#parseJSONtoRecipeList(Context, String)}
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        if (event.getMessageType().equals(Config.EVENTBUS_MESSAGE_TYPE_EXCEPTION)) {
            binding.jsonExceptionImage.setVisibility(View.VISIBLE);
        }
        Toast.makeText(RecipesListActivity.this, event.getMessage(), Toast.LENGTH_LONG).show();
    }
}
