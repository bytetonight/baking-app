package android.example.com.foodoo.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.example.com.foodoo.R;
import android.example.com.foodoo.adapters.StepAdapter;
import android.example.com.foodoo.config.Config;
import android.example.com.foodoo.databinding.ActivityStepListBinding;
import android.example.com.foodoo.handlers.ClickActionCallback;
import android.example.com.foodoo.models.BaseModel;
import android.example.com.foodoo.models.Recipe;
import android.example.com.foodoo.models.Step;
import android.example.com.foodoo.ui.fragments.StepDetailFragment;
import android.example.com.foodoo.utils.ColorTools;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * An activity representing a list of {@link Step}. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link StepDetailActivity} representing {@link Step} details.
 * On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class StepListActivity extends AppCompatActivity
        implements ClickActionCallback, StepDetailFragment.OnStepDetailNavigationClickObserver {


    /**
     * 2p or not 2p, that is the question.
     * I.e. whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean inTwoPaneMode;
    private Recipe recipe;
    private ActivityStepListBinding binding;
    private int currentStepIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_step_list);
        binding.setActivityHandledClick(this);
        //binding.stepListInc.ingredientsButtonInc.setActivityHandledClick(this);

        //((DaggerApplication) getApplication()).getAppComponent().inject(this);


        Intent senderIntent = getIntent();
        if (senderIntent != null) {
            if (savedInstanceState != null || senderIntent.hasExtra(Config.DATA_KEY_RECIPE)) {
                recipe = senderIntent.getParcelableExtra(Config.DATA_KEY_RECIPE);
            }
        }

        if (savedInstanceState != null) {
            recipe = savedInstanceState.getParcelable(Config.DATA_KEY_RECIPE);
        }

        setTitle(recipe.getName());

        if (binding.stepListInc.stepDetailContainer != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-sw600dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            inTwoPaneMode = true;
        }

        RecyclerView stepsRecyclerView = binding.stepListInc.stepList;
        if (stepsRecyclerView != null) {
            setupRecyclerView(stepsRecyclerView);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(Config.DATA_KEY_RECIPE, recipe);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * Prepare the RecyclerView for use in a method to reduce redundant code.
     * I think this came from the Android Studio template for Master/Detail flows
     *
     * @param recyclerView is the View to setup
     */
    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(StepListActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        List<? extends BaseModel> steps = recipe.getSteps();
        steps = ColorTools.colorizeList(steps, getResources().getColor(R.color.startColor),
                getResources().getColor(R.color.endColor));
        recyclerView.setAdapter(new StepAdapter(steps));
    }

    /**
     * Start {@link StepDetailActivity} OR
     * if {@link StepListActivity#inTwoPaneMode} is true
     * start up the {@link StepDetailFragment}
     */
    @Override
    public void stepOnClick(View v, Step step) {
        currentStepIndex = step.getPosition();

        if (inTwoPaneMode) {
            // In inTwoPaneMode (Tablet-Mode) we start and host the StepDetailFragment right here
            Bundle arguments = new Bundle();
            arguments.putParcelable(Config.DATA_KEY_STEP, step);
            StepDetailFragment fragment = new StepDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(binding.stepListInc.stepDetailContainer.getId(), fragment)
                    .commit();
        } else {
            /*
             *  In Non-Tablet-Mode we redirect to {@link StepDetailActivity}
             *  which starts and hosts the same StepDetailsFragment
             */
            Intent intent = new Intent(this, StepDetailActivity.class);
            intent.putExtra(Config.DATA_KEY_STEP, step);
            intent.putExtra(Config.DATA_KEY_STEP_INDEX, currentStepIndex);
            intent.putParcelableArrayListExtra(Config.DATA_KEY_STEP_LIST, ((ArrayList<? extends Parcelable>) recipe.getSteps()));
            startActivity(intent);
        }
    }

    /**
     * Head over to {@link IngredientsListActivity}
     * and pass the {@link Recipe} that was clicked
     *
     * @param v is the View that was clicked
     */
    @Override
    public void ingredientsButtonOnClick(View v) {
        Intent intent = new Intent(this, IngredientsListActivity.class);
        intent.putExtra(Config.DATA_KEY_RECIPE, recipe);
        startActivity(intent);
    }


    @Override
    public void onCurrentStepDetailChanged(int currentStepIndex) {
        if (currentStepIndex >= recipe.getSteps().size() - 1) {
            currentStepIndex = recipe.getSteps().size() - 1;
        } else if (currentStepIndex <= 0) {
            currentStepIndex = 0;
        }


    }
}
