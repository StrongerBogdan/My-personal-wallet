package com.bogdanmurzin.domain.usecases

import com.bogdanmurzin.domain.entities.AccountType
import com.bogdanmurzin.domain.repositories.AccountRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllAccountTypesUseCase @Inject constructor(private val accountRepository: AccountRepository) {

    suspend operator fun invoke(): Flow<List<AccountType>> =
        accountRepository.getAllAccountTypes()
}