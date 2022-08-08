package com.bogdanmurzin.mypersonalwallet.mapper

import com.bogdanmurzin.domain.entities.Transaction
import com.bogdanmurzin.mypersonalwallet.data.transaction_recycer_items.TransactionItemUiModel
import java.text.DecimalFormat
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

    fun toTransaction(item: TransactionItemUiModel): Transaction {
        val s = Transaction(
            item.id,
            trxCategoryUiMapper.toTrxCategory(item.category),
            item.date,
            item.description,
            item.accountType,
            parse(item.transactionAmount)
        )
        return s
    }

    fun parse(amount: String): Float {
        val locate = Locale.getDefault()
        val format = NumberFormat.getNumberInstance(locate)
        if (format is DecimalFormat) {
            format.isParseBigDecimal = true
        }
        return format.parse(amount.replace("[^\\d.,]".toRegex(), "")) as Float
    }
}



