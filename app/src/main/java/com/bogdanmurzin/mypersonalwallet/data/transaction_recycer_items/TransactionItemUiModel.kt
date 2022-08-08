package com.bogdanmurzin.mypersonalwallet.data.transaction_recycer_items

import com.bogdanmurzin.domain.entities.AccountType
import com.bogdanmurzin.domain.entities.TransactionCategory
import com.bogdanmurzin.mypersonalwallet.adapter.MyMoneyTransactionRecyclerViewAdapter
import com.bogdanmurzin.mypersonalwallet.data.TrxCategoryUiModel
import java.util.*

data class TransactionItemUiModel(
    val id: Int,
    val category: TrxCategoryUiModel,
    val date: Date,
    val description: String?,
    val accountType: AccountType,
    val transactionAmount: String
) : RecyclerMultiTypeItem {
    override val type = MyMoneyTransactionRecyclerViewAdapter.ITEM_TYPE_TRANSACTION
    var selected: Boolean = false
}