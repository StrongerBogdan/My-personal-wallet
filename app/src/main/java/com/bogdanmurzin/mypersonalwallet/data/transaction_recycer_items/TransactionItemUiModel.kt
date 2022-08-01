package com.bogdanmurzin.mypersonalwallet.data.transaction_recycer_items

import com.bogdanmurzin.domain.entities.AccountType
import com.bogdanmurzin.domain.entities.TransactionCategory
import com.bogdanmurzin.mypersonalwallet.adapter.MyMoneyTransactionRecyclerViewAdapter
import java.util.*

data class TransactionItemUiModel(
    val id: Int,
    val category: TransactionCategory,
    val date: Date,
    val description: String?,
    val accountType: AccountType,
    override val transactionAmount: Float
) : RecyclerMultiTypeItem {
    override val type = MyMoneyTransactionRecyclerViewAdapter.ITEM_TYPE_TRANSACTION
}