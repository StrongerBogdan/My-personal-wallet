package com.bogdanmurzin.data.repositories.transaction

import com.bogdanmurzin.data.db.AccountTypeDao
import com.bogdanmurzin.data.db.TransactionCategoryDao
import com.bogdanmurzin.data.db.TransactionsDao
import com.bogdanmurzin.data.mapper.TransactionsEntityMapper
import com.bogdanmurzin.domain.entities.Transaction
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TransactionsLocalDataSourceImpl @Inject constructor(
    private val transactionsDao: TransactionsDao,
    private val accountTypeDao: AccountTypeDao,
    private val transactionCategoryDao: TransactionCategoryDao,
    private val dispatcher: CoroutineDispatcher,
    private val transactionsEntityMapper: TransactionsEntityMapper
) : TransactionsLocalDataSource {

    override suspend fun getLocalTransactions(): Flow<List<Transaction>> =
        withContext(dispatcher) {
            val savedTransactionsFlow = transactionsDao.getAll()
            return@withContext savedTransactionsFlow.map { list ->
                list.map { element ->
                    val transactionCategory =
                        transactionCategoryDao.getTrxCategoryById(element.transactionCategoryId)
                    val accountType = accountTypeDao.getAccountTypeById(element.accountTypeId)
                    transactionsEntityMapper.toTransaction(
                        element,
                        transactionCategory,
                        accountType
                    )
                }
            }
        }

    override suspend fun getLocalTransactionById(id: Int): Transaction =
        withContext(dispatcher) {
            val transactionEntity = transactionsDao.getById(id)

            val transactionCategory =
                transactionCategoryDao.getTrxCategoryById(transactionEntity.transactionCategoryId)
            val accountType = accountTypeDao.getAccountTypeById(transactionEntity.accountTypeId)
            transactionsEntityMapper.toTransaction(
                transactionEntity,
                transactionCategory,
                accountType
            )
        }

    override suspend fun insertTransaction(transaction: Transaction) =
        withContext(dispatcher) {
            transactionsDao.insert(
                transactionsEntityMapper.toTransactionEntity(
                    transaction,
                    transaction.category.id,
                    transaction.accountType.id
                )
            )
        }

    override suspend fun updateTransaction(transaction: Transaction) =
        withContext(dispatcher) {
            transactionsDao.update(
                transactionsEntityMapper.toTransactionEntity(
                    transaction,
                    transaction.category.id,
                    transaction.accountType.id
                )
            )
        }

    override suspend fun deleteTransactions(transactionIds: List<Int>) =
        withContext(dispatcher) {
            if (transactionIds.isNotEmpty()) {
                transactionsDao.deleteTransactions(transactionIds)
            }
        }

}