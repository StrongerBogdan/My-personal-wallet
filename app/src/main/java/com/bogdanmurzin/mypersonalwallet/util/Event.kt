package com.bogdanmurzin.mypersonalwallet.util

sealed class Event {
    data class OpenPreviewScreen(val id: Int) : Event()
    data class OpenCategoryScreen(val type: CategoryArg) : Event()
    data class Error(val exception: Exception) : Event()
}
