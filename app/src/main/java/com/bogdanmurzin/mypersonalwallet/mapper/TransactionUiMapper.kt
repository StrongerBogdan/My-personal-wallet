package com.bogdanmurzin.mypersonalwallet.mapper

import com.bogdanmurzin.domain.entities.Transaction
import com.bogdanmurzin.mypersonalwallet.data.transaction_recycer_items.TransactionItemUiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import java.text.NumberFormat
import java.util.*
import javax.inject.Inject

class TransactionUiMapper @Inject constructor(private val trxCategoryUiMapper: TrxCategoryUiMapper) {

    fun toListOfTransactionUiModel(list: List<Transaction>): List<TransactionItemUiModel> =
        list.map {
            toTransactionUiModel(it)
        }

    fun toTransactionUiModel(item: Transaction): TransactionItemUiModel {
        val locate = Locale.getDefault()
        return TransactionItemUiModel(
            item.id,
            trxCategoryUiMapper.toTrxCategoryUiModel(item.category),
            item.date,
            item.description,
            item.accountType,
            NumberFormat.getCurrencyInstance(locate).format(item.transactionAmount)
        )
    }
}



