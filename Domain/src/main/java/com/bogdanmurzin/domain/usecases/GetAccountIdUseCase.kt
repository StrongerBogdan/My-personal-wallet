package com.bogdanmurzin.domain.usecases

import com.bogdanmurzin.domain.entities.AccountType
import com.bogdanmurzin.domain.repositories.AccountRepository
import javax.inject.Inject

class GetAccountIdUseCase @Inject constructor(private val accountRepository: AccountRepository) {

    suspend operator fun invoke(account: AccountType): Int =
        accountRepository.getAccountId(account)
}