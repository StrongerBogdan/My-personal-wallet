package com.bogdanmurzin.data.repositories.transaction_category

import com.bogdanmurzin.data.db.AccountTypeDao
import com.bogdanmurzin.data.db.TransactionCategoryDao
import com.bogdanmurzin.data.mapper.TransactionCategoryEntityMapper
import com.bogdanmurzin.domain.entities.TransactionCategory
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
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
            val flow = transactionCategoryDao.getAllTrxCategories()
            return@withContext flow.map { list ->
                list.map { item ->
                    transactionCategoryEntityMapper.toTransactionCategory(item)
                }
            }
        }


    override suspend fun getTrxCategoryId(trxCategory: TransactionCategory): Int =
        withContext(dispatcher) {
            transactionCategoryDao.getTrxCategoryId(trxCategory.title, trxCategory.imageUri)
        }
}