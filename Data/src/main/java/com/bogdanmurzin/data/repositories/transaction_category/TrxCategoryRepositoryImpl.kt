package com.bogdanmurzin.data.repositories.transaction_category

import com.bogdanmurzin.domain.entities.AccountType
import com.bogdanmurzin.domain.entities.Transaction
import com.bogdanmurzin.domain.entities.TransactionCategory
import com.bogdanmurzin.domain.repositories.AccountRepository
import com.bogdanmurzin.domain.repositories.TransactionRepository
import com.bogdanmurzin.domain.repositories.TrxCategoryRepository
import kotlinx.coroutines.flow.Flow
import java.util.*
import javax.inject.Inject

class TrxCategoryRepositoryImpl @Inject constructor(
    private val localDataSource: TrxCategoryLocalDataSource
) : TrxCategoryRepository {

    override suspend fun getTrxCategoryById(id: Int): TransactionCategory =
        localDataSource.getTrxCategoryById(id)

    override suspend fun getAllTrxCategories(): Flow<List<TransactionCategory>> =
        localDataSource.getAllTrxCategories()

    override suspend fun getTrxCategoryId(trxCategory: TransactionCategory): Int =
        localDataSource.getTrxCategoryId(trxCategory)

}