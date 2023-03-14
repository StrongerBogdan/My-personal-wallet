package com.bogdanmurzin.data.mapper

import com.bogdanmurzin.data.entity.TransactionCategoryEntity
import com.bogdanmurzin.domain.entities.TransactionCategory
import javax.inject.Inject

class TransactionCategoryEntityMapper @Inject constructor() {

    fun toTransactionCategory(item: TransactionCategoryEntity): TransactionCategory =
        TransactionCategory(item.id, item.title, item.subcategory, item.imageUri)

    fun toTransactionCategoryEntity(item: TransactionCategory): TransactionCategoryEntity =
        TransactionCategoryEntity(item.id, item.title, item.subcategory, item.imageUri)
}