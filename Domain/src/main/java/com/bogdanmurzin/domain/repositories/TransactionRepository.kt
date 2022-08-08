package com.bogdanmurzin.domain.repositories

import com.bogdanmurzin.domain.entities.Transaction
import kotlinx.coroutines.flow.Flow
import java.util.*

interface TransactionRepository {

    suspend fun getLocalTransactions(): Flow<List<Transaction>>

    suspend fun getLocalTransactionById(id: Int): Transaction

    suspend fun getLocalDateTransaction(date: Date? = null): Transaction

    suspend fun insertTransactions(transactions: List<Transaction>): List<Transaction>

    suspend fun insertTransaction(transaction: Transaction)

    suspend fun updateTransaction(transaction: Transaction)

    suspend fun deleteTransactions(transactionIds: List<Int>)
}