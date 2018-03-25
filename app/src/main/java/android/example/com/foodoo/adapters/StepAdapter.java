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
import android.example.com.foodoo.databinding.StepListItemBinding;
import android.example.com.foodoo.handlers.ClickActionCallback;
import android.example.com.foodoo.models.BaseModel;
import android.example.com.foodoo.models.Step;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.ViewHolder> {

    private List<? extends BaseModel> steps;


    public StepAdapter(List<? extends BaseModel> steps) {
        this.steps = steps;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        StepListItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.step_list_item, parent, false);

        binding.setClickActionCallback( (ClickActionCallback) parent.getContext());
        return new ViewHolder(binding);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Step step = (Step)steps.get(position);
        step.setPosition(position);
        holder.bind(step);
    }


    @Override
    public int getItemCount() {
        if (steps != null)
            return steps.size();
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
            binding.setVariable(BR.step, obj);
            binding.executePendingBindings();
        }
    }

}
