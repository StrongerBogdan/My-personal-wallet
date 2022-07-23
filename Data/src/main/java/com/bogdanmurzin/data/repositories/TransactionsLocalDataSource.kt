package com.bogdanmurzin.data.repositories

import com.bogdanmurzin.domain.entities.Transaction
import kotlinx.coroutines.flow.Flow
import java.util.*

interface TransactionsLocalDataSource {

    suspend fun getLocalTransactions(): Flow<List<Transaction>>
    suspend fun getLocalDateTransaction(date: Date? = null): Transaction

    suspend fun insertTransactions(
        transactions: List<Transaction>
    ): List<Transaction>

    suspend fun deleteTransactions()
}