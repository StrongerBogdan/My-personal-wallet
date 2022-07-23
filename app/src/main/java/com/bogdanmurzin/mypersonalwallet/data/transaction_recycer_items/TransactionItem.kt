package com.bogdanmurzin.mypersonalwallet.data.transaction_recycer_items

import com.bogdanmurzin.domain.entities.AccountType
import com.bogdanmurzin.domain.entities.MoneyTransactionCategory
import com.bogdanmurzin.mypersonalwallet.adapter.MyMoneyTransactionRecyclerViewAdapter
import java.math.BigDecimal

data class TransactionItem(
    val category: MoneyTransactionCategory,
    val description: String?,
    val accountType: AccountType,
    override val transactionAmount: BigDecimal
) : RecyclerMultiTypeItem {
    override val type = MyMoneyTransactionRecyclerViewAdapter.ITEM_TYPE_TRANSACTION
}