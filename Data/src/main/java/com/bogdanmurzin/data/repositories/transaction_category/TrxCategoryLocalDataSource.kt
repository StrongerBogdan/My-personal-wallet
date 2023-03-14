package com.bogdanmurzin.data.repositories.transaction_category

import com.bogdanmurzin.domain.entities.TransactionCategory
import kotlinx.coroutines.flow.Flow

interface TrxCategoryLocalDataSource {

    suspend fun getTrxCategoryById(id: Int): TransactionCategory

    suspend fun getAllTrxCategories(): Flow<List<TransactionCategory>>

    suspend fun getTrxCategoryId(trxCategory: TransactionCategory): Int

    suspend fun getAllTrxSubCategories(title: String): Flow<List<TransactionCategory>>

    suspend fun getTrxCategoryBySubcategory(title: String, subcategory: String?)
            : TransactionCategory

    suspend fun insertTrxCategory(trxCategory: TransactionCategory)

    suspend fun updateTrxCategory(trxCategory: TransactionCategory)

    suspend fun deleteTrxCategory(id: Int)

}