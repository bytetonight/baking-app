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
import android.example.com.foodoo.databinding.IngredientListItemBinding;
import android.example.com.foodoo.models.BaseModel;
import android.example.com.foodoo.models.Ingredient;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.ViewHolder> {

    private List<? extends BaseModel> ingredients;


    public IngredientsAdapter(List<? extends BaseModel> ingredients) {
        this.ingredients = ingredients;
    }

    /**
     * Used to set the data after the Adapter has been initialized
     * @param ingredients
     */
    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        IngredientListItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.ingredient_list_item, parent, false);

        return new ViewHolder(binding);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Ingredient ingredient = (Ingredient) ingredients.get(position);
        holder.bind(ingredient);
    }


    @Override
    public int getItemCount() {
        if (ingredients != null)
            return ingredients.size();
        return 0;
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
            binding.setVariable(BR.ingredient, obj);
            binding.executePendingBindings();
        }
    }

}
