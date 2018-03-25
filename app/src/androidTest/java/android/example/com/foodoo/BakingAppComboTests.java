package android.example.com.foodoo;


import android.content.Context;
import android.example.com.foodoo.ui.RecipesListActivity;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.DisplayMetrics;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.not;

/**
 * Created by ByteTonight on 23.02.2018.
 *
 * The requirements:
 * UI Testing: Application makes use of Espresso to test aspects of the UI.
 *
 * Result: this class tests "aspects" of the UI. Does it test all aspects of the UI ?
 * No, the requirements don't say so
 *
 */
@RunWith(AndroidJUnit4.class)
public class BakingAppComboTests {

    /*
    * https://developer.android.com/training/testing/espresso/idling-resource.html
    * Basically just needed to tell the test-environment : There's an async task running
    * and you need to wait for its results before you jump to the actual test
    */
    private IdlingResource idlingResource;
    private static final float TABLET_LAYOUT_MIN_SCREEN_SIZE = 600f; // Layout constraint = sw600dp


    private boolean isTabletMode(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth  = displayMetrics.widthPixels / displayMetrics.density;
        return dpWidth >= TABLET_LAYOUT_MIN_SCREEN_SIZE;
    }

    @Rule public ActivityTestRule<RecipesListActivity> recipesListActivityTestRule =
            new ActivityTestRule<>(RecipesListActivity.class);

    @Before
    public void registerIdlingResource()
    {
        idlingResource = recipesListActivityTestRule.getActivity().getNetworkNetworkIdlingResource();
        IdlingRegistry.getInstance().register(idlingResource);
    }

    /**
     * Is there anything (first RecyclerViewListItem in this case) clickable ?
     * Will only pass if JSON has been loaded and images from JSON have been loaded too
     */
    @Test
    public void recipesListActivityDataDisplayed()
    {
        // https://developer.android.com/training/testing/espresso/lists.html#recycler-view-list-items
        onView(withId(R.id.recipes_list_recycler_view)).check(matches(isDisplayed()));

        onView(withId(R.id.recipes_list_recycler_view)).check(
                new RecyclerViewItemCountAssertion(
                        RecyclerViewItemCountAssertion.AssertionOption.GREATER_THAN_ZERO)
        );

        onView(withId(R.id.recipes_list_recycler_view))
        .perform(RecyclerViewActions
        .actionOnItemAtPosition(0, click()));
    }



    @Test
    public void stepListActivityDataDisplayed()
    {
        recipesListActivityDataDisplayed();

        onView(withId(R.id.step_list))
                .perform(RecyclerViewActions
                        .actionOnItemAtPosition(1, click()));

        Context context = InstrumentationRegistry.getTargetContext();


        // the paging buttons in step detail should not be visible in tablet mode
        if (isTabletMode(context)) {
            onView(withId(R.id.step_previous)).check(matches(not(isDisplayed())));
            onView(withId(R.id.step_next)).check(matches(not(isDisplayed())));
        } else {
            onView(withId(R.id.step_previous)).check(matches(isDisplayed()));
            onView(withId(R.id.step_next)).check(matches(isDisplayed()));
        }
    }



    @Test
    public void ingredientsListActivityDataDisplayed()
    {
        recipesListActivityDataDisplayed();

        // What ever !
        // the actual button to perform the click on has the id R.id.ingredients_button
        // and this button is included into the layout.
        // the include tag itself has the id used below, which suddenly works
        onView(withId(R.id.ingredients_button_inc)).perform(click());

        //onView(allOf(withId(R.id.ingredients_button_inc), hasSibling(withId(R.id.ingredients_button)))).perform(click());

        onView(withId(R.id.ingredients_list))
                .perform(RecyclerViewActions
                        .actionOnItemAtPosition(0, click()));
    }

    @After
    public void unregisterIdlingResource() {
        if (idlingResource != null) {
            IdlingRegistry.getInstance().unregister(idlingResource);
        }
    }
}
