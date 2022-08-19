package com.bogdanmurzin.data.repositories.transaction

import com.bogdanmurzin.domain.entities.Transaction
import kotlinx.coroutines.flow.Flow

interface TransactionsLocalDataSource {

    suspend fun getLocalTransactions(): Flow<List<Transaction>>

    suspend fun getLocalTransactionById(id: Int): Transaction

    suspend fun insertTransaction(transaction: Transaction)

    suspend fun updateTransaction(transaction: Transaction)

    suspend fun deleteTransactions(transactionIds: List<Int>)
}