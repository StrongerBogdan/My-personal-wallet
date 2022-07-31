package com.bogdanmurzin.data.mapper

import com.bogdanmurzin.data.entity.TransactionCategoryEntity
import com.bogdanmurzin.data.entity.TransactionEntity
import com.bogdanmurzin.domain.entities.TransactionCategory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TransactionCategoryEntityMapper @Inject constructor() {

    fun toTransactionCategory(item: TransactionCategoryEntity): TransactionCategory =
        TransactionCategory(item.title, item.subcategory, item.imageUri)

    fun toFlowOfTransactionCategory(flow: Flow<List<TransactionCategoryEntity>>): Flow<List<TransactionCategory>> =
        flow.map { list ->
            list.map { item ->
                toTransactionCategory(item)
            }
        }
}