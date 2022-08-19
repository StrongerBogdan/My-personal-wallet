package com.bogdanmurzin.data.repositories.transaction

import com.bogdanmurzin.domain.entities.Transaction
import com.bogdanmurzin.domain.repositories.TransactionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(
    private val localDataSource: TransactionsLocalDataSource
) : TransactionRepository {

    override suspend fun getLocalTransactions(): Flow<List<Transaction>> =
        localDataSource.getLocalTransactions()

    override suspend fun getLocalTransactionById(id: Int): Transaction =
        localDataSource.getLocalTransactionById(id)

    override suspend fun insertTransaction(transaction: Transaction) =
        localDataSource.insertTransaction(transaction)

    override suspend fun updateTransaction(transaction: Transaction) =
        localDataSource.updateTransaction(transaction)

    override suspend fun deleteTransactions(transactionIds: List<Int>) {
        localDataSource.deleteTransactions(transactionIds)
    }
}