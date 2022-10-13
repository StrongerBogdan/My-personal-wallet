package com.bogdanmurzin.mypersonalwallet.util

import android.view.inputmethod.EditorInfo
import android.widget.EditText

object Extensions {

    fun EditText.onDone(callback: () -> Unit) {
        setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                callback.invoke()
                return@setOnEditorActionListener true
            }
            false
        }
    }
}