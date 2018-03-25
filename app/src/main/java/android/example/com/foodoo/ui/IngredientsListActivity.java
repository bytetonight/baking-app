package android.example.com.foodoo.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.example.com.foodoo.DaggerApplication;
import android.example.com.foodoo.R;
import android.example.com.foodoo.adapters.IngredientsAdapter;
import android.example.com.foodoo.config.Config;
import android.example.com.foodoo.databinding.ActivityIngredientsListBinding;
import android.example.com.foodoo.models.BaseModel;
import android.example.com.foodoo.models.Ingredient;
import android.example.com.foodoo.models.Recipe;
import android.example.com.foodoo.utils.ColorTools;
import android.example.com.foodoo.widgets.IngredientsWidgetUpdateService;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

public class IngredientsListActivity extends AppCompatActivity {

    private Recipe recipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityIngredientsListBinding binding =
                DataBindingUtil.setContentView(this, R.layout.activity_ingredients_list);
        Intent senderIntent = getIntent();

        ((DaggerApplication) getApplication()).getAppComponent().inject(this);

        if (senderIntent.hasExtra(Config.DATA_KEY_RECIPE)) {
            recipe = senderIntent.getParcelableExtra(Config.DATA_KEY_RECIPE);
        }

        if (savedInstanceState != null) {
            recipe = savedInstanceState.getParcelable(Config.DATA_KEY_RECIPE);
        }
        setTitle(recipe.getName());
        RecyclerView stepsRecyclerView =  binding.ingredientsList;
        if (stepsRecyclerView != null) {
            setupRecyclerView(stepsRecyclerView);
        }

        startIngredientsWidgetUpdateService();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(Config.DATA_KEY_RECIPE, recipe);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        recipe = savedInstanceState.getParcelable(Config.DATA_KEY_RECIPE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        startIngredientsWidgetUpdateService();
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        RecyclerView.LayoutManager layoutManager =
                new LinearLayoutManager(IngredientsListActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        List<? extends BaseModel> ingredients = recipe.getIngredients();
        ingredients = ColorTools.colorizeList(ingredients,
                getResources().getColor(R.color.startColor),
                getResources().getColor(R.color.endColor));

        recipe.setIngredients((List<Ingredient>)ingredients);
        recyclerView.setAdapter(new IngredientsAdapter(ingredients));
    }

    /**
     * Start up the Widget Update Service and pass the whole recipe to the service
     * just like I've been doing throughout the entire code base
     */
    private void startIngredientsWidgetUpdateService()
    {
        Intent intent = new Intent(this, IngredientsWidgetUpdateService.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(Config.DATA_KEY_RECIPE, recipe);
        intent.putExtra(Config.DATA_KEY_RECIPE_BUNDLE, bundle);
        intent.setAction(IngredientsWidgetUpdateService.ACTION_UPDATE_INGREDIENTS_WIDGET);
        startService(intent);
    }


}
