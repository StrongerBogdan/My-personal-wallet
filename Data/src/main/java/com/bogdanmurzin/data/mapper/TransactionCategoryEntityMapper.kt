package com.bogdanmurzin.data.mapper

import com.bogdanmurzin.data.entity.TransactionCategoryEntity
import com.bogdanmurzin.domain.entities.TransactionCategory
import javax.inject.Inject

class TransactionCategoryEntityMapper @Inject constructor() {

    fun toTransactionCategory(item: TransactionCategoryEntity): TransactionCategory =
        TransactionCategory(item.title, item.subcategory, item.imageUri)
}