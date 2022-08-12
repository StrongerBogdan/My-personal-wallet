package com.bogdanmurzin.mypersonalwallet.mapper

import com.bogdanmurzin.mypersonalwallet.data.transaction_recycer_items.HeaderItemUiModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class HeaderItemUiMapper @Inject constructor() {

    private val locate = Locale.getDefault()
    val dayFormat = SimpleDateFormat("dd", locate)
    val dayOfTheWeekFormat = SimpleDateFormat("EEE", locate)
    val monthYearFormat = SimpleDateFormat("MMMM yyyy", locate)

    fun toHeaderItemUiMapper(date: Date, sumOfTransactionsAmount: Float): HeaderItemUiModel {
        val locate = Locale.getDefault()

        return HeaderItemUiModel(
            day = dayFormat.format(date),
            dayOfTheWeek = dayOfTheWeekFormat.format(date).uppercase(),
            monthYear = monthYearFormat.format(date).uppercase(),
            sumOfTransactions = NumberFormat.getCurrencyInstance(locate).format(sumOfTransactionsAmount)
        )
    }
}