package android.example.com.foodoo;

import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.ViewAssertion;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.number.OrderingComparison.greaterThan;

/**
 * https://stackoverflow.com/a/37339656
 * Extended and modified by ByteTonight
 *
 * How to use :
 * onView(withId(R.id.recyclerView)).check(new RecyclerViewItemCountAssertion(5));
 */


public class RecyclerViewItemCountAssertion implements ViewAssertion {

    public enum AssertionOption {
        GREATER_THAN_ZERO, EUUAL_TO_PARAM
    }

    private int expectedCount = 0;
    private AssertionOption option;

    public RecyclerViewItemCountAssertion(AssertionOption option, int expectedCount) {
        this.option = option;
        this.expectedCount = expectedCount;
    }

    public RecyclerViewItemCountAssertion(AssertionOption option) {
        this.option = option;
    }

    @Override
    public void check(View view, NoMatchingViewException noViewFoundException) {
        if (noViewFoundException != null) {
            throw noViewFoundException;
        }

        RecyclerView recyclerView = (RecyclerView) view;
        RecyclerView.Adapter adapter = recyclerView.getAdapter();

        switch (option) {
            case EUUAL_TO_PARAM:
                assertThat(adapter.getItemCount(), is(expectedCount));
                break;
            case GREATER_THAN_ZERO:
                assertThat(adapter.getItemCount(), greaterThan(0));
                break;
        }
    }
}
