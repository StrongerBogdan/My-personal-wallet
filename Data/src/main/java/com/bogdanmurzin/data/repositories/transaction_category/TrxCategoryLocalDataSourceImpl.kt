package com.bogdanmurzin.data.repositories.transaction_category

import com.bogdanmurzin.data.db.TransactionCategoryDao
import com.bogdanmurzin.data.mapper.TransactionCategoryEntityMapper
import com.bogdanmurzin.domain.entities.TransactionCategory
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TrxCategoryLocalDataSourceImpl @Inject constructor(
    private val transactionCategoryDao: TransactionCategoryDao,
    private val dispatcher: CoroutineDispatcher,
    private val transactionCategoryEntityMapper: TransactionCategoryEntityMapper
) : TrxCategoryLocalDataSource {

    override suspend fun getTrxCategoryById(id: Int): TransactionCategory =
        withContext(dispatcher) {
            val result = transactionCategoryDao.getTrxCategoryById(id)
            return@withContext transactionCategoryEntityMapper.toTransactionCategory(result)
        }

    override suspend fun getAllTrxCategories(): Flow<List<TransactionCategory>> =
        withContext(dispatcher) {
            transactionCategoryEntityMapper.toFlowOfTransactionCategory(
                transactionCategoryDao.getAllTrxCategories()
            )
        }

    override suspend fun getTrxCategoryId(trxCategory: TransactionCategory): Int =
        withContext(dispatcher) {
            transactionCategoryDao.getTrxCategoryId(trxCategory.title, trxCategory.imageUri)
        }

    override suspend fun getAllTrxSubCategories(title: String): Flow<List<TransactionCategory>> =
        withContext(dispatcher) {
            transactionCategoryEntityMapper.toFlowOfTransactionCategory(
                transactionCategoryDao.getAllTrxSubCategories(title)
            )
        }

    override suspend fun getTrxCategoryIdBySubcategory(title: String, subcategory: String?): Int =
        withContext(dispatcher) {
            return@withContext if (subcategory == null) {
                transactionCategoryDao
                    .getTrxCategoryIdBySubcategory(title)
            } else {
                transactionCategoryDao
                    .getTrxCategoryIdBySubcategory(title, subcategory)
            }
        }
}