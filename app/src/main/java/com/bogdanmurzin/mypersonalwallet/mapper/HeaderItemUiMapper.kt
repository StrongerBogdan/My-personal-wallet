package com.bogdanmurzin.mypersonalwallet.mapper

import com.bogdanmurzin.mypersonalwallet.data.transaction_recycer_items.HeaderItemUiModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class HeaderItemUiMapper @Inject constructor(private val locale: Locale) {

    private val dayFormat = SimpleDateFormat("dd", locale)
    private val dayOfTheWeekFormat = SimpleDateFormat("EEEE", locale)
    private val monthYearFormat = SimpleDateFormat("MMMM yyyy", locale)

    fun toHeaderItemUiMapper(date: Date, sumOfTransactionsAmount: Float): HeaderItemUiModel {

        return HeaderItemUiModel(
            day = dayFormat.format(date),
            dayOfTheWeek = dayOfTheWeekFormat.format(date).uppercase(),
            monthYear = monthYearFormat.format(date).uppercase(),
            sumOfTransactions = NumberFormat.getCurrencyInstance(locale)
                .format(sumOfTransactionsAmount)
        )
    }
}