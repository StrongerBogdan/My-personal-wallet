package com.bogdanmurzin.domain.usecases.account_type

import com.bogdanmurzin.domain.entities.AccountType
import com.bogdanmurzin.domain.entities.Transaction
import com.bogdanmurzin.domain.repositories.AccountRepository
import com.bogdanmurzin.domain.repositories.TransactionRepository
import javax.inject.Inject

class InsertAccountUseCase @Inject constructor(private val accountRepository: AccountRepository) {

    suspend operator fun invoke(accountType: AccountType) {
        accountRepository.insertAccount(accountType)
    }
}