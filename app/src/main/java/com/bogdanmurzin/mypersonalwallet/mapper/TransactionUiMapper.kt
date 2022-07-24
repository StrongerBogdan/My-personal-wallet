package com.bogdanmurzin.mypersonalwallet.mapper

import android.util.Log
import com.bogdanmurzin.domain.entities.Transaction
import com.bogdanmurzin.mypersonalwallet.data.transaction_recycer_items.TransactionItemUiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TransactionUiMapper @Inject constructor() {

    fun toTransactionUiModel(item: Flow<List<Transaction>>): Flow<List<TransactionItemUiModel>> =
        item.map {
            it.map { element ->
                TransactionItemUiModel(
                    element.category,
                    element.description,
                    element.accountType,
                    element.transactionAmount
                ).also {
                    Log.i("TAGG", "toTransactionUiModel: ${it}")
                }

            }
        }
}



