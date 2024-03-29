package com.bogdanmurzin.domain.usecases.transaction

import com.bogdanmurzin.domain.entities.Transaction
import com.bogdanmurzin.domain.repositories.TransactionRepository
import javax.inject.Inject

class InsertTransactionUseCase @Inject constructor(private val transactionRepository: TransactionRepository) {

    suspend operator fun invoke(transaction: Transaction) {
        transactionRepository.insertTransaction(transaction)
    }
}