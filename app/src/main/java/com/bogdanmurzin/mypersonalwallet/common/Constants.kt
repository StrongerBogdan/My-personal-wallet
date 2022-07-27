package com.bogdanmurzin.mypersonalwallet.common

import com.bogdanmurzin.domain.entities.AccountType

object Constants {
    const val ICON_SCALE = 100
    const val TAG = "DEBUG_TAGG"
    val DEFAULT_ACCOUNT = AccountType("Default wallet", "file:///android_asset/wallet_icon.png")
}