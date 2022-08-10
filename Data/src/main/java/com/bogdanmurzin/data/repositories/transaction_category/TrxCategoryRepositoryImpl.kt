package com.bogdanmurzin.data.repositories.transaction_category

import com.bogdanmurzin.domain.entities.TransactionCategory
import com.bogdanmurzin.domain.repositories.TrxCategoryRepository
import kotlinx.coroutines.flow.Flow
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

    override suspend fun getAllTrxSubCategories(title: String): Flow<List<TransactionCategory>> =
        localDataSource.getAllTrxSubCategories(title)

    override suspend fun getTrxCategoryIdBySubcategory(title: String, subcategory: String?): Int =
        localDataSource.getTrxCategoryIdBySubcategory(title, subcategory)

    override suspend fun insertTrxCategory(trxCategory: TransactionCategory) =
        localDataSource.insertTrxCategory(trxCategory)

    override suspend fun updateTrxCategory(trxCategory: TransactionCategory) =
        localDataSource.updateTrxCategory(trxCategory)

}