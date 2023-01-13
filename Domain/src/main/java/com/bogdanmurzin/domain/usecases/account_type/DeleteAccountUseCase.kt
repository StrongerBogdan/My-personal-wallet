package com.bogdanmurzin.domain.usecases.account_type

import com.bogdanmurzin.domain.repositories.AccountRepository
import javax.inject.Inject

class DeleteAccountUseCase @Inject constructor(private val accountRepository: AccountRepository) {

    suspend operator fun invoke(id: Int) {
        accountRepository.deleteAccountById(id)
    }
}