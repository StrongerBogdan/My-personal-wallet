package com.bogdanmurzin.domain.usecases.transaction

import com.bogdanmurzin.domain.entities.Transaction
import com.bogdanmurzin.domain.repositories.TransactionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeleteTransactionsUseCase @Inject constructor(private val transactionRepository: TransactionRepository) {

    suspend operator fun invoke(transactionIds: List<Int>) {
        transactionRepository.deleteTransactions(transactionIds)
    }
}