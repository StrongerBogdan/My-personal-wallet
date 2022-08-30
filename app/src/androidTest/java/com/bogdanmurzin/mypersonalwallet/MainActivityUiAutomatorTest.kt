package com.bogdanmurzin.mypersonalwallet

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.*
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class MainActivityUiAutomatorTest {

    @get : Rule
    var hiltRule = HiltAndroidRule(this)

    private val device: UiDevice

    init {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        device = UiDevice.getInstance(instrumentation)
    }

    @Before
    fun startMainActivityFromHomeScreen() {
        hiltRule.inject()
        // Start from the home screen
        device.pressHome()

        // Wait for launcher
        val launchPackage: String? = getLauncherPackageName()
        assertNotNull(launchPackage)
        device.wait(Until.hasObject(By.pkg(launchPackage).depth(0)), LAUNCH_TIMEOUT)

        // Launch the app
        val context: Context = getApplicationContext()
        val intent = context.packageManager.getLaunchIntentForPackage(APP_PACKAGE)?.apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
        assertNotNull(intent)
        context.startActivity(intent)

        // Wait for the app to appear
        device.wait(
            Until.hasObject(By.pkg(APP_PACKAGE).depth(0)), LAUNCH_TIMEOUT
        )
    }

    @Test
    fun openAddTransactionBottomSheet() {
        val fab = device.findObject(By.res(APP_PACKAGE, "fab"))
        fab.click()

        val result = device.wait(
            Until.hasObject(By.res(APP_PACKAGE, "transaction_amount_tv")), LAUNCH_TIMEOUT
        )

        assertTrue("Dialog didn't opened", result)
    }

    private fun getLauncherPackageName(): String? {
        // Create launcher Intent
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)

        // Use PackageManager to get the launcher package name
        val pm = getApplicationContext<Context>().packageManager
        val resolveInfo = pm.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY)
        return resolveInfo?.activityInfo?.packageName
    }

    companion object {
        const val LAUNCH_TIMEOUT = 2000L
        const val APP_PACKAGE = "com.bogdanmurzin.mypersonalwallet.debug"
    }
}