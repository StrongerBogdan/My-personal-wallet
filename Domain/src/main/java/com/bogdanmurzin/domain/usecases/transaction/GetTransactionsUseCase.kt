package com.bogdanmurzin.domain.usecases.transaction

import com.bogdanmurzin.domain.entities.Transaction
import com.bogdanmurzin.domain.repositories.TransactionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTransactionsUseCase @Inject constructor(private val transactionRepository: TransactionRepository) {

    suspend operator fun invoke(): Flow<List<Transaction>> =
        transactionRepository.getLocalTransactions()
}