package com.bogdanmurzin.data.repositories.transaction

import com.bogdanmurzin.domain.entities.Transaction
import kotlinx.coroutines.flow.Flow
import java.util.*

interface TransactionsLocalDataSource {

    suspend fun getLocalTransactions(): Flow<List<Transaction>>

    suspend fun getLocalDateTransaction(date: Date? = null): Transaction

    suspend fun getLocalTransactionById(id: Int): Transaction

    suspend fun insertTransactions(transactions: List<Transaction>): List<Transaction>

    suspend fun insertTransaction(transaction: Transaction)

    suspend fun updateTransaction(transaction: Transaction)

    suspend fun deleteTransactions()
}