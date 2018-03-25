package android.example.com.foodoo.ui;

import android.content.Intent;
import android.example.com.foodoo.R;
import android.example.com.foodoo.config.Config;
import android.example.com.foodoo.models.Step;
import android.example.com.foodoo.ui.fragments.StepDetailFragment;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import java.util.List;

/**
 * An activity representing a single {@link Step} detail screen. This
 * ACTIVITY is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link StepListActivity}.
 */
public class StepDetailActivity extends AppCompatActivity
        implements StepDetailFragment.OnStepDetailNavigationClickObserver {

    private Step step;
    private List<Step> steps = null;
    private int currentStepIndex;
    private int lastIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);

        Intent senderIntent = getIntent();

        if (senderIntent.hasExtra(Config.DATA_KEY_STEP)) {
            step = senderIntent.getParcelableExtra(Config.DATA_KEY_STEP);
            currentStepIndex = step.getPosition();
        }

        if (senderIntent.hasExtra(Config.DATA_KEY_STEP_INDEX)) {
            currentStepIndex = senderIntent.getIntExtra(Config.DATA_KEY_STEP_INDEX, 0);
        }

        if (senderIntent.hasExtra(Config.DATA_KEY_STEP_LIST)) {
            steps = senderIntent.getParcelableArrayListExtra(Config.DATA_KEY_STEP_LIST);
            if (steps != null && step == null) {
                step = steps.get(currentStepIndex);
            }
        }

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putParcelable(Config.DATA_KEY_STEP,
                    getIntent().getParcelableExtra(Config.DATA_KEY_STEP));
            arguments.putInt(Config.DATA_KEY_LAST_STEP_INDEX, steps.size()-1);
            StepDetailFragment fragment = new StepDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.step_detail_container, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            NavUtils.navigateUpTo(this, new Intent(this, StepListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCurrentStepDetailChanged(int currentStepIndex) {
        //Toast.makeText(this, currentStepIndex+"", Toast.LENGTH_SHORT).show();
        if (currentStepIndex > steps.size() - 1) {
            currentStepIndex = steps.size() - 1;
            return;
        } else if (currentStepIndex < 0) {
            currentStepIndex = 0;
            return;
        }

        Bundle arguments = new Bundle();
        arguments.putParcelable(Config.DATA_KEY_STEP, steps.get(currentStepIndex));
        arguments.putInt(Config.DATA_KEY_LAST_STEP_INDEX, steps.size()-1);
        StepDetailFragment fragment = new StepDetailFragment();
        fragment.setArguments(arguments);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.step_detail_container, fragment)
                .commit();
    }
}
