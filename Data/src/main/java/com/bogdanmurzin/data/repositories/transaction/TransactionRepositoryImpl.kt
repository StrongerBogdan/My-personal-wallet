package com.bogdanmurzin.data.repositories.transaction

import com.bogdanmurzin.domain.entities.Transaction
import com.bogdanmurzin.domain.repositories.TransactionRepository
import kotlinx.coroutines.flow.Flow
import java.util.*
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(
    private val localDataSource: TransactionsLocalDataSource
) : TransactionRepository {

    override suspend fun getLocalTransactions(): Flow<List<Transaction>> =
        localDataSource.getLocalTransactions()

    override suspend fun getLocalDateTransaction(date: Date?): Transaction =
        localDataSource.getLocalDateTransaction(date)

    override suspend fun insertTransactions(transactions: List<Transaction>): List<Transaction> =
        localDataSource.insertTransactions(transactions)

    override suspend fun deleteTransactions() {
        localDataSource.deleteTransactions()
    }
}