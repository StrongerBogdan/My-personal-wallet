package com.bogdanmurzin.mypersonalwallet.ui.fragment

import com.bogdanmurzin.mypersonalwallet.util.EditingState

interface IBottomSheetAdd<in T> {
    fun done(args: T, editingState: EditingState)
    fun setupViewModel()
}