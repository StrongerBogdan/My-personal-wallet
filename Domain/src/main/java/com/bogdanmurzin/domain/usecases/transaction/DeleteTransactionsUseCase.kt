package com.bogdanmurzin.domain.usecases.transaction

import com.bogdanmurzin.domain.repositories.TransactionRepository
import javax.inject.Inject

class DeleteTransactionsUseCase @Inject constructor(private val transactionRepository: TransactionRepository) {

    suspend operator fun invoke(transactionIds: List<Int>) {
        transactionRepository.deleteTransactions(transactionIds)
    }
}