package com.bogdanmurzin.mypersonalwallet.mapper

import com.bogdanmurzin.mypersonalwallet.data.transaction_recycer_items.HeaderItemUiModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class HeaderItemUiMapper @Inject constructor() {

    fun toHeaderItemUiMapper(date: Date, sumOfTransactionsAmount: Float): HeaderItemUiModel {
        val locate = Locale.getDefault()

        return HeaderItemUiModel(
            SimpleDateFormat("dd", locate).format(date),
            SimpleDateFormat("EEE", locate).format(date).uppercase(),
            SimpleDateFormat("MMMM yyyy", locate).format(date).uppercase(),
            NumberFormat.getCurrencyInstance(locate).format(sumOfTransactionsAmount)
        )
    }
}