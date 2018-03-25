package android.example.com.foodoo.loaders;

import android.content.Context;
import android.example.com.foodoo.models.BaseModel;
import android.example.com.foodoo.network.RecipeRepository;
import android.support.v4.content.AsyncTaskLoader;

import java.util.List;

/**
 * Created by ByteTonight on 10.06.2017.
 */

public class RecipeLoader extends AsyncTaskLoader<List<? extends BaseModel>> {

    public RecipeLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<? extends BaseModel> loadInBackground() {
        return RecipeRepository.loadRecipes(getContext());
    }

}
