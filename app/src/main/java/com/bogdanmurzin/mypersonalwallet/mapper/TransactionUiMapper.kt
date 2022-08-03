package com.bogdanmurzin.mypersonalwallet.mapper

import com.bogdanmurzin.domain.entities.Transaction
import com.bogdanmurzin.mypersonalwallet.data.transaction_recycer_items.TransactionItemUiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.text.NumberFormat
import java.util.*
import javax.inject.Inject

class TransactionUiMapper @Inject constructor(private val trxCategoryUiMapper: TrxCategoryUiMapper) {

    fun toFlowOfTransactionUiModel(item: Flow<List<Transaction>>): Flow<List<TransactionItemUiModel>> {
        val locate = Locale.getDefault()
        return item.map {
            it.map { element ->
                TransactionItemUiModel(
                    element.id,
                    trxCategoryUiMapper.toTrxCategoryUiModel(element.category),
                    element.date,
                    element.description,
                    element.accountType,
                    NumberFormat.getCurrencyInstance(locate).format(element.transactionAmount)
                )
            }
        }
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



