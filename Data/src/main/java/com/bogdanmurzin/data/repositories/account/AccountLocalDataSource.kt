package com.bogdanmurzin.data.repositories.account

import com.bogdanmurzin.domain.entities.AccountType
import com.bogdanmurzin.domain.entities.Transaction
import kotlinx.coroutines.flow.Flow
import java.util.*

interface AccountLocalDataSource {

    suspend fun getAccountTypeById(id: Int): AccountType

    suspend fun getAllAccountTypes(): Flow<List<AccountType>>

    suspend fun getAccountId(account: AccountType): Int

}