package com.bogdanmurzin.data.mapper

import com.bogdanmurzin.data.entity.TransactionCategoryEntity
import com.bogdanmurzin.domain.entities.MoneyTransactionCategory
import javax.inject.Inject

class TransactionCategoryEntityMapper @Inject constructor() {

    fun toTransactionCategory(item: TransactionCategoryEntity): MoneyTransactionCategory =
        MoneyTransactionCategory(item.title, item.subcategory, item.transactionPicUri)
}