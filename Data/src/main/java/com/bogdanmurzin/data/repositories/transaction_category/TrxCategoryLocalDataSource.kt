package com.bogdanmurzin.data.repositories.transaction_category

import com.bogdanmurzin.domain.entities.AccountType
import com.bogdanmurzin.domain.entities.Transaction
import com.bogdanmurzin.domain.entities.TransactionCategory
import kotlinx.coroutines.flow.Flow
import java.util.*

interface TrxCategoryLocalDataSource {

    suspend fun getTrxCategoryById(id: Int): TransactionCategory

    suspend fun getAllTrxCategories(): Flow<List<TransactionCategory>>

    suspend fun getTrxCategoryId(trxCategory: TransactionCategory): Int

}