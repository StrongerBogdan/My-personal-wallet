package com.bogdanmurzin.domain.repositories

import com.bogdanmurzin.domain.entities.AccountType
import com.bogdanmurzin.domain.entities.Transaction
import kotlinx.coroutines.flow.Flow
import java.util.*

interface AccountRepository {

    suspend fun getAccountTypeById(id: Int): AccountType

    suspend fun getAllAccountTypes(): Flow<List<AccountType>>

    suspend fun getAccountId(account: AccountType): Int

    suspend fun updateAccount(account: AccountType)

    suspend fun insertAccount(account: AccountType)

}