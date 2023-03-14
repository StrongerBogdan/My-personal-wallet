package com.bogdanmurzin.mypersonalwallet.ui.activity

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.bogdanmurzin.mypersonalwallet.R
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.IsInstanceOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class AccountTypeFragmentTest {

    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun addNewAccountType() {
        val bottomNavigationItemView = onView(
            allOf(
                withId(R.id.accountFragment), withContentDescription("Account"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.bottom_nav_view),
                        0
                    ), 0
                )
            )
        )
        bottomNavigationItemView.perform(click())

        val actionMenuItemView = onView(
            allOf(
                withId(R.id.m_add), withContentDescription("Add"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.toolbar),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        actionMenuItemView.perform(click())

        val frameLayout = onView(
            allOf(withId(R.id.account_container), isDisplayed())
        )
        frameLayout.perform(click())

        val searchAutoComplete = onView(withId(R.id.search))
        searchAutoComplete.perform(typeText("frog"), closeSoftKeyboard())


        val recyclerView = onView(
            allOf(
                withId(R.id.recycler),
                childAtPosition(
                    withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                    2
                )
            )
        )
        // Wait search
        Thread.sleep(5000)
        recyclerView.perform(actionOnItemAtPosition<ViewHolder>(29, click()))

        val materialAutoCompleteTextView = onView(
            allOf(withId(R.id.account_title), isDisplayed())
        )
        materialAutoCompleteTextView.perform(replaceText("Lyagushka"), closeSoftKeyboard())

        val materialAutoCompleteTextView2 = onView(
            allOf(withId(R.id.account_title), withText("Lyagushka"), isDisplayed())
        )
        materialAutoCompleteTextView2.perform(pressImeActionButton())

        val viewGroup = onView(
            allOf(
                withParent(
                    allOf(
                        withId(R.id.category_recycler),
                        withParent(withId(R.id.account_type_layout))
                    )
                ),
                isDisplayed()
            )
        )
        viewGroup.check(matches(isDisplayed()))

        val bottomNavigationItemView2 = onView(
            allOf(
                withId(R.id.fragmentMoneyTransactions), withContentDescription("Home"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.bottom_nav_view),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        bottomNavigationItemView2.perform(click())

        val floatingActionButton = onView(
            allOf(
                withId(R.id.fab), withContentDescription("Add Transaction"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.nav_host_fragment),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        floatingActionButton.perform(click())

        val materialCardView = onView(
            allOf(withId(R.id.account_cv), isDisplayed())
        )
        materialCardView.perform(click())

        val viewGroup2 = onView(
            allOf(
                withParent(
                    allOf(
                        withId(R.id.account_recycler),
                        withParent(IsInstanceOf.instanceOf(android.view.ViewGroup::class.java))
                    )
                ),
                isDisplayed()
            )
        )
        viewGroup2.check(matches(isDisplayed()))
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }

}
