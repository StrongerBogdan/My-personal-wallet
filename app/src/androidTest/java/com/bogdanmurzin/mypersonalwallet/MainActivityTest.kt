package com.bogdanmurzin.mypersonalwallet

import android.content.Context
import android.content.SharedPreferences
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.bogdanmurzin.data.db.AppDatabase
import com.bogdanmurzin.domain.entities.AccountType
import com.bogdanmurzin.domain.entities.TransactionCategory
import com.bogdanmurzin.domain.usecases.account_type.InsertAccountUseCase
import com.bogdanmurzin.domain.usecases.transaction_category.InsertTrxCategoryUseCase
import com.bogdanmurzin.mypersonalwallet.common.Constants
import com.bogdanmurzin.mypersonalwallet.di.AppModule
import com.bogdanmurzin.mypersonalwallet.ui.activity.MainActivity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.hamcrest.Matchers.allOf
import org.hamcrest.core.IsInstanceOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@UninstallModules(AppModule::class)
@HiltAndroidTest
class MainActivityTest {

    @get : Rule
    var hiltRule = HiltAndroidRule(this)

    @get : Rule
    var mActivityRule = ActivityScenarioRule(MainActivity::class.java)

    @Module
    @InstallIn(SingletonComponent::class)
    class FakeDBModule {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        @Provides
        fun provideInMemoryDB(@ApplicationContext context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                val instance = Room.inMemoryDatabaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java
                )
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }

        @Provides
        fun provideSharedPreference(@ApplicationContext context: Context): SharedPreferences =
            context.getSharedPreferences(Constants.PREF_THEME_COLOR, Context.MODE_PRIVATE)

        @Provides
        fun provideLocale(): Locale = Locale.getDefault()
    }

    private val locale = Locale.getDefault()
    private val dayFormat = SimpleDateFormat("dd", locale)
    private val dayOfTheWeekFormat = SimpleDateFormat("EEEE", locale)
    private val monthYearFormat = SimpleDateFormat("MMMM yyyy", locale)

    @Inject
    lateinit var addTrxCategoryUseCase: InsertTrxCategoryUseCase

    @Inject
    lateinit var addAccountUseCase: InsertAccountUseCase

    @Before
    fun setUp() {
        hiltRule.inject()
        runBlocking(Dispatchers.IO) {
            addAccountUseCase.invoke(createAccount())
            addTrxCategoryUseCase.invoke(createTrxCategory())
        }

    }

    private fun createAccount() =
        AccountType(
            1,
            ACCOUNT_TYPE_TITLE,
            "https://cdn1.iconfinder.com/data/icons/material-core/20/account-circle-256.png"
        )

    private fun createTrxCategory() =
        TransactionCategory(
            1,
            TRX_CATEGORY_TITLE,
            null,
            "https://cdn1.iconfinder.com/data/icons/aami-web-internet/64/aami3-24-256.png"
        )

    @Test
    fun add1TransactionWithoutNoteWithoutSubcategoryDefaultDate() {
        // open Add new transaction
        onView(withId(R.id.fab)).perform(click())
        // choose account type
        onView(withId(R.id.account_cv)).perform(click())
        onView(withId(R.id.account_recycler)).perform(
            actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click())
        )
        // choose transaction category
        onView(withId(R.id.transaction_cv)).perform(click())
        onView(withId(R.id.account_recycler)).perform(
            actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click())
        )
        onView(withId(R.id.done_btn)).perform(click())
        // write transaction amount
        onView(withId(R.id.transaction_amount_tv)).perform(typeText(TRANSACTION_AMOUNT_TYPED))
        onView(withId(R.id.transaction_amount_tv)).perform(pressImeActionButton())
        onView(withId(R.id.transaction_description)).perform(pressImeActionButton())
        // Assertion

        val date = Date()
        val day = dayFormat.format(date)
        val dayOfTheWeek = dayOfTheWeekFormat.format(date).uppercase()
        val monthYear = monthYearFormat.format(date).uppercase()

        onView(withId(R.id.category_tv)).check(ViewAssertions.matches(withText(TRX_CATEGORY_TITLE)))
        onView(withId(R.id.account_type_tv))
            .check(ViewAssertions.matches(withText(ACCOUNT_TYPE_TITLE)))
        onView(
            allOf(
                withId(R.id.transaction_amount_tv), withText(TRANSACTION_AMOUNT_SHOWED),
                ViewMatchers.withParent(ViewMatchers.withParent(IsInstanceOf.instanceOf(FrameLayout::class.java))),
                ViewMatchers.isDisplayed()
            )
        ).check(ViewAssertions.matches(withText(TRANSACTION_AMOUNT_SHOWED)))

        onView(withId(R.id.day_tv)).check(ViewAssertions.matches(withText(day)))
        onView(withId(R.id.day_of_the_week_tv)).check(ViewAssertions.matches(withText(dayOfTheWeek)))
        onView(withId(R.id.month_year_tv)).check(ViewAssertions.matches(withText(monthYear)))
    }


    companion object {
        const val TRX_CATEGORY_TITLE = "TrxCategory1"
        const val ACCOUNT_TYPE_TITLE = "Account1"
        const val TRANSACTION_AMOUNT_TYPED = "1337"
        const val TRANSACTION_AMOUNT_SHOWED = "$13.37"
    }

}