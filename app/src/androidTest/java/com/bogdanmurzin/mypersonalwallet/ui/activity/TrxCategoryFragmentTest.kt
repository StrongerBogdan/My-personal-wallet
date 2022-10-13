package com.bogdanmurzin.mypersonalwallet.ui.activity


import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
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
class TrxCategoryFragmentTest {

    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun addTrxCategoryWithoutSubcategory() {
        val bottomNavigationItemView = onView(
            allOf(
                withId(R.id.categoryFragment), withContentDescription("Category"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.bottom_nav_view),
                        0
                    ),
                    2
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
            allOf(withId(R.id.trxcategory_container), isDisplayed())
        )
        frameLayout.perform(click())

        val searchAutoComplete = onView(
            allOf(withId(R.id.search), isDisplayed())
        )
        searchAutoComplete.perform(typeText("text"), closeSoftKeyboard())

        // Wait search
        Thread.sleep(5000)

        val recyclerView = onView(
            allOf(
                withId(R.id.recycler),
                childAtPosition(
                    withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                    2
                )
            )
        )
        recyclerView.perform(actionOnItemAtPosition<ViewHolder>(1, click()))

        val materialAutoCompleteTextView = onView(
            allOf(withId(R.id.trxcategory_category), isDisplayed())
        )
        materialAutoCompleteTextView.perform(replaceText("Biography"), closeSoftKeyboard())

        val materialAutoCompleteTextView2 = onView(
            allOf(withId(R.id.trxcategory_category), withText("Biography"), isDisplayed())
        )
        materialAutoCompleteTextView2.perform(pressImeActionButton())

        val viewGroup = onView(
            allOf(
                withParent(
                    allOf(
                        withId(R.id.category_recycler),
                        withParent(withId(R.id.transaction_category_layout))
                    )
                ),
                isDisplayed()
            )
        )
        viewGroup.check(matches(isDisplayed()))

        val bottomNavigationItemView3 = onView(
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
        bottomNavigationItemView3.perform(click())

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
            allOf(withId(R.id.transaction_cv), isDisplayed())
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

    @Test
    fun addTrxCategoryWithSubcategoryTest() {
        val bottomNavigationItemView = onView(
            allOf(
                withId(R.id.categoryFragment), withContentDescription("Category"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.bottom_nav_view),
                        0
                    ),
                    2
                ),
                isDisplayed()
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
            allOf(withId(R.id.trxcategory_container), isDisplayed())
        )
        frameLayout.perform(click())

        val searchAutoComplete = onView(
            allOf(withId(R.id.search), isDisplayed())
        )
        searchAutoComplete.perform(typeText("text"), closeSoftKeyboard())

        val recyclerView = onView(
            allOf(
                withId(R.id.recycler),
                childAtPosition(
                    withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                    2
                )
            )
        )
        Thread.sleep(5000)
        recyclerView.perform(actionOnItemAtPosition<ViewHolder>(1, click()))

        val materialAutoCompleteTextView = onView(
            allOf(withId(R.id.trxcategory_category), isDisplayed())
        )
        materialAutoCompleteTextView.perform(replaceText("Biograaphy"), closeSoftKeyboard())

        val materialAutoCompleteTextView2 = onView(
            allOf(withId(R.id.trxcategory_category), withText("Biograaphy"), isDisplayed())
        )
        materialAutoCompleteTextView2.perform(pressImeActionButton())

        val recyclerView2 = onView(
            allOf(
                withId(R.id.category_recycler),
                childAtPosition(
                    withId(R.id.transaction_category_layout),
                    0
                )
            )
        )
        recyclerView2.perform(actionOnItemAtPosition<ViewHolder>(0, click()))

        val chip = onView(
            childAtPosition(
                allOf(
                    withId(R.id.subcategory_chip_group),
                    childAtPosition(
                        withId(R.id.subcategories_scroll),
                        0
                    )
                ),
                0
            )
        )
        chip.perform(scrollTo(), click())

        val materialAutoCompleteTextView3 = onView(
            allOf(
                withId(R.id.subcategory_title),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        materialAutoCompleteTextView3.perform(replaceText("test1"), closeSoftKeyboard())

        val materialButton = onView(
            allOf(
                withId(R.id.done_btn), withText("Done"),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        materialButton.perform(click())

        val radioButton = onView(
            allOf(
                withText("test1"),
                withParent(
                    allOf(
                        withId(R.id.subcategory_chip_group),
                        withParent(withId(R.id.subcategories_scroll))
                    )
                ),
                isDisplayed()
            )
        )
        radioButton.check(matches(isDisplayed()))

        val chip2 = onView(
            childAtPosition(
                allOf(
                    withId(R.id.subcategory_chip_group),
                    childAtPosition(
                        withId(R.id.subcategories_scroll),
                        0
                    )
                ),
                0
            )
        )
        chip2.perform(scrollTo(), click())

        val materialAutoCompleteTextView4 = onView(
            allOf(
                withId(R.id.subcategory_title),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        materialAutoCompleteTextView4.perform(replaceText("test2"), closeSoftKeyboard())

        val materialButton2 = onView(
            allOf(
                withId(R.id.done_btn), withText("Done"),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        materialButton2.perform(click())

        val radioButton2 = onView(
            allOf(
                withText("test2"),
                withParent(
                    allOf(
                        withId(R.id.subcategory_chip_group),
                        withParent(withId(R.id.subcategories_scroll))
                    )
                ),
                isDisplayed()
            )
        )
        radioButton2.check(matches(isDisplayed()))

        val materialButton3 = onView(
            allOf(withId(R.id.done_btn), withText("Done"), isDisplayed())
        )
        materialButton3.perform(click())

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
            allOf(withId(R.id.transaction_cv), isDisplayed())
        )
        materialCardView.perform(click())

        val recyclerView3 = onView(
            allOf(
                withId(R.id.account_recycler),
                childAtPosition(
                    withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                    1
                )
            )
        )
        recyclerView3.perform(actionOnItemAtPosition<ViewHolder>(0, click()))

        val radioButton3 = onView(
            allOf(
                withText("test1"),
                withParent(
                    allOf(
                        withId(R.id.subcategory_chip_group),
                        withParent(withId(R.id.subcategories_scroll))
                    )
                ),
                isDisplayed()
            )
        )
        radioButton3.check(matches(isDisplayed()))

        val radioButton4 = onView(
            allOf(
                withText("test2"),
                withParent(
                    allOf(
                        withId(R.id.subcategory_chip_group),
                        withParent(withId(R.id.subcategories_scroll))
                    )
                ),
                isDisplayed()
            )
        )
        radioButton4.check(matches(isDisplayed()))

        val chip3 = onView(
            allOf(
                withText("test2"),
                childAtPosition(
                    allOf(
                        withId(R.id.subcategory_chip_group),
                        childAtPosition(
                            withId(R.id.subcategories_scroll),
                            0
                        )
                    ),
                    1
                )
            )
        )
        chip3.perform(scrollTo(), click())

        val radioButton5 = onView(
            allOf(
                withText("test2"),
                withParent(
                    allOf(
                        withId(R.id.subcategory_chip_group),
                        withParent(withId(R.id.subcategories_scroll))
                    )
                ),
                isDisplayed()
            )
        )
        radioButton5.check(matches(isChecked()))

        val materialButton4 = onView(
            allOf(
                withId(R.id.done_btn), withText("Done"),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0
                    ),
                    3
                ),
                isDisplayed()
            )
        )
        materialButton4.perform(click())

        val textView = onView(
            allOf(
                withId(R.id.category_entity_subcategory_tv), withText("(test2)"),
                withParent(
                    allOf(
                        withId(R.id.transaction_category_type),
                        withParent(withId(R.id.transaction_cv))
                    )
                ),
                isDisplayed()
            )
        )
        textView.check(matches(withText("(test2)")))
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
