package com.bogdanmurzin.mypersonalwallet.data.transaction_recycer_items

import com.bogdanmurzin.mypersonalwallet.adapter.MyMoneyTransactionRecyclerViewAdapter
import java.math.BigDecimal
import java.util.*

data class HeaderItemUiModel(
    val day: String,
    val dayOfTheWeek: String,
    val monthYear: String,
    val sumOfTransactions: String,
) : RecyclerMultiTypeItem {
    override val type = MyMoneyTransactionRecyclerViewAdapter.ITEM_TYPE_HEADER
}