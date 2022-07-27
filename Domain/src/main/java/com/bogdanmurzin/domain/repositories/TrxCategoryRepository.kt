package com.bogdanmurzin.domain.repositories

import com.bogdanmurzin.domain.entities.TransactionCategory
import kotlinx.coroutines.flow.Flow

interface TrxCategoryRepository {

    suspend fun getTrxCategoryById(id: Int): TransactionCategory

    suspend fun getAllTrxCategories(): Flow<List<TransactionCategory>>

    suspend fun getTrxCategoryId(trxCategory: TransactionCategory): Int

}