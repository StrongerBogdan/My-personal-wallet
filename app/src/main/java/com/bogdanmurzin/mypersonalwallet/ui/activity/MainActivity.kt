package com.bogdanmurzin.mypersonalwallet.ui.activity

import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.bogdanmurzin.mypersonalwallet.R
import com.bogdanmurzin.mypersonalwallet.common.Constants
import com.bogdanmurzin.mypersonalwallet.common.Constants.PREF_THEME_COLOR
import com.bogdanmurzin.mypersonalwallet.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var preferences: SharedPreferences

    var listener =
        OnSharedPreferenceChangeListener { prefs, key ->
            if (key == PREF_THEME_COLOR) {
                val themeResId = prefs.getInt(PREF_THEME_COLOR, Constants.DEFAULT_THEME)
                theme.applyStyle(themeResId, true)
                recreate()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val themeResId = preferences.getInt(PREF_THEME_COLOR, Constants.DEFAULT_THEME)
        theme.applyStyle(themeResId, true)

        preferences.registerOnSharedPreferenceChangeListener(listener)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar?.inflateMenu(R.menu.add_menu)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNavView.setupWithNavController(navController)
    }
}