package com.bogdanmurzin.mypersonalwallet.mapper

import android.util.Log
import com.bogdanmurzin.domain.entities.Transaction
import com.bogdanmurzin.mypersonalwallet.data.transaction_recycer_items.TransactionItemUiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TransactionUiMapper @Inject constructor() {

    fun toFlowOfTransactionUiModel(item: Flow<List<Transaction>>): Flow<List<TransactionItemUiModel>> =
        item.map {
            it.map { element ->
                TransactionItemUiModel(
                    element.id,
                    element.category,
                    element.date,
                    element.description,
                    element.accountType,
                    element.transactionAmount
                )
            }
        }

    fun toTransactionUiModel(item: Transaction): TransactionItemUiModel =
        TransactionItemUiModel(
            item.id,
            item.category,
            item.date,
            item.description,
            item.accountType,
            item.transactionAmount
        )

}



