package com.bogdanmurzin.mypersonalwallet.ui.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bogdanmurzin.mypersonalwallet.common.Constants.DEFAULT_PREF_REMINDER
import com.bogdanmurzin.mypersonalwallet.common.Constants.DEFAULT_THEME
import com.bogdanmurzin.mypersonalwallet.common.Constants.PREF_REMINDER
import com.bogdanmurzin.mypersonalwallet.common.Constants.PREF_THEME_COLOR
import com.bogdanmurzin.mypersonalwallet.util.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val preferences: SharedPreferences
) : ViewModel() {

    private val _reminderOn = MutableLiveData<Boolean>()
    val reminderOn: LiveData<Boolean> = _reminderOn
    private val _action: SingleLiveEvent<Event> = SingleLiveEvent()
    val action: LiveData<Event> = _action

    init {
        loadPrefs()
    }

    private fun loadPrefs() {
        val isReminderOn = preferences.getBoolean(PREF_REMINDER, DEFAULT_PREF_REMINDER)
        _reminderOn.postValue(isReminderOn)
    }

    fun changeReminderSwitch(isReminderSwitchOn: Boolean) {
        _reminderOn.postValue(isReminderSwitchOn)
        updateReminderPref(isReminderSwitchOn)
    }

    fun changeThemeColor(themeId: Int) {
        updateThemePref(themeId)
    }

    private fun updateThemePref(themeId: Int) {
        with(preferences.edit()) {
            putInt(
                PREF_THEME_COLOR,
                themeId
            )
            apply()
        }
    }

    private fun updateReminderPref(isReminderSwitchOn: Boolean) {
        with(preferences.edit()) {
            putBoolean(
                PREF_REMINDER,
                isReminderSwitchOn
            )
            apply()
        }
    }

    fun openThemeColorChooser() {
        _action.postValue(Event.OpenThemeColorChooser)
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(
        private val sharedPreferences: SharedPreferences
    ) : ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            SettingsViewModel(
                sharedPreferences
            ) as T
    }
}