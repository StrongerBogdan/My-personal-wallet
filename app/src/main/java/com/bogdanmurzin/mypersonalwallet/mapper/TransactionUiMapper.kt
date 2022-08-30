package com.bogdanmurzin.mypersonalwallet.mapper

import com.bogdanmurzin.domain.entities.Transaction
import com.bogdanmurzin.mypersonalwallet.data.transaction_recycer_items.TransactionItemUiModel
import java.text.NumberFormat
import java.util.*
import javax.inject.Inject

class TransactionUiMapper @Inject constructor(
    private val trxCategoryUiMapper: TrxCategoryUiMapper,
    private val locale: Locale
) {

    fun toListOfTransactionUiModel(
        list: List<Transaction>,
        selectedTransactions: List<Int> = listOf()
    ): List<TransactionItemUiModel> =
        list.map {
            val isSelected = selectedTransactions.contains(it.id)
            toTransactionUiModel(it, isSelected)
        }.sortedByDescending { it.date }

    fun toTransactionUiModel(
        item: Transaction,
        isSelected: Boolean = false
    ): TransactionItemUiModel {
        return TransactionItemUiModel(
            item.id,
            trxCategoryUiMapper.toTrxCategoryUiModel(item.category),
            item.date,
            item.description,
            item.accountType,
            NumberFormat.getCurrencyInstance(locale).format(item.transactionAmount),
            isSelected
        )
    }
}



