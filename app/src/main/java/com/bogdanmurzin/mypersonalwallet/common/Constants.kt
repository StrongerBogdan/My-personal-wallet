package com.bogdanmurzin.mypersonalwallet.common

import com.bogdanmurzin.mypersonalwallet.R

object Constants {
    // Shared prefs
    const val PREF_THEME_COLOR = "shared_pref_theme_color"
    const val DEFAULT_THEME = R.style.OverlayPrimaryColorGreen
    const val PREF_REMINDER = "shared_pref_reminder"
    const val DEFAULT_PREF_REMINDER = false

    // UI
    const val ICON_SCALE = 100
    const val SPAN_COUNT = 4

    // Util
    const val TAG = "DEBUG_TAGG"
    const val EMPTY_STRING = ""
    val DOLLAR_OR_COMA_REGEX = Regex("[$,]")
    val DOLLAR_OR_COMA_OR_DOT_REGEX = Regex("[$,.]")
    const val RETROFIT_ICONS_NAMED = "RetrofitIconsClient"

    // Result keys
    const val COLOR_RESULT = "color_result"
    const val SETTINGS_RESULT = "settings_result"
    const val DATE_RESULT_KEY = "date_result_key"
    const val TRX_CATEGORY_RESULT_KEY = "trx_category_result_key"
    const val ACCOUNT_TYPE_RESULT_KEY = "account_type_result_key"

    // Notification
    const val NOTIFICATION_ID = 1
    const val NOTIFICATION_NAME = "Reminder"
    const val NOTIFICATION_CHANNEL = "Default notification"
    const val NOTIFICATION_WORKER_TAG = "appName_notification_work"
    const val REQUEST_CODE = 0
    const val FLAGS = 0
}