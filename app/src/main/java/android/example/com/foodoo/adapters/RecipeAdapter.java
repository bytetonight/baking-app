/*
 * PopularMovies by bytetonight
 * Created for the Udacity (c) Android (c) Developer Nanodegree
 * This software uses a remote API kindly made accessible by
 * https://www.themoviedb.org/
 *
 * Copyright (c) 2017.
 */

package android.example.com.foodoo.adapters;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.example.com.foodoo.BR;
import android.example.com.foodoo.R;
import android.example.com.foodoo.databinding.RecipeListItemBinding;
import android.example.com.foodoo.handlers.DataBindingClickHandler;
import android.example.com.foodoo.models.BaseModel;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {

    private List<? extends BaseModel> recipes;

    public RecipeAdapter(List<? extends BaseModel> recipes) {
        this.recipes = recipes;
    }

    /**
     * Used to set the data after the Adapter has been initialized
     * @param recipes
     */
    public void setRecipes(List<? extends BaseModel> recipes) {
        this.recipes = recipes;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

         RecipeListItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.recipe_list_item, parent, false);

        binding.setClickHandler(new DataBindingClickHandler());
        return new ViewHolder(binding);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(recipes.get(position));
    }


    @Override
    public int getItemCount() {
        return recipes != null ? recipes.size() : 0;
    }


    public final class ViewHolder extends RecyclerView.ViewHolder {
        private ViewDataBinding binding;

        /**
         * @param binding of type ViewDataBinding which is an
         *                abstract Base Class for generated binding classes
         */
        public ViewHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Object obj) {
            binding.setVariable(BR.recipe, obj);
            binding.executePendingBindings();
        }
    }

}
