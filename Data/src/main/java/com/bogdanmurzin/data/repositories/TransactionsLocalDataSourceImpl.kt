package com.bogdanmurzin.data.repositories

import com.bogdanmurzin.data.db.AccountTypeDao
import com.bogdanmurzin.data.db.TransactionCategoryDao
import com.bogdanmurzin.data.db.TransactionsDao
import com.bogdanmurzin.data.mapper.TransactionsEntityMapper
import com.bogdanmurzin.domain.entities.Transaction
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.*
import javax.inject.Inject

class TransactionsLocalDataSourceImpl @Inject constructor(
    private val transactionsDao: TransactionsDao,
    private val accountTypeDao: AccountTypeDao,
    private val transactionCategoryDao: TransactionCategoryDao,
    private val dispatcher: CoroutineDispatcher,
    private val transactionsEntityMapper: TransactionsEntityMapper
) : TransactionsLocalDataSource {

    override suspend fun getLocalTransactions(): Flow<List<Transaction>> {
        val savedTransactionsFlow = transactionsDao.getAll()
        return savedTransactionsFlow.map { list ->
            list.map { element ->
                val transactionCategory =
                    transactionCategoryDao.getTransactionCategory(element.transactionCategoryId)
                val accountType = accountTypeDao.getAccountType(element.accountTypeId)
                transactionsEntityMapper.toTransaction(element, transactionCategory, accountType)
            }
        }
    }

    override suspend fun getLocalDateTransaction(date: Date?): Transaction {
        TODO("Not yet implemented")
    }

    override suspend fun insertTransactions(transactions: List<Transaction>): List<Transaction> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteTransactions() {
        TODO("Not yet implemented")
    }
}