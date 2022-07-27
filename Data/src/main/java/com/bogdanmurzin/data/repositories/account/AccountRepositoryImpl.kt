package com.bogdanmurzin.data.repositories.account

import com.bogdanmurzin.domain.entities.AccountType
import com.bogdanmurzin.domain.entities.Transaction
import com.bogdanmurzin.domain.repositories.AccountRepository
import com.bogdanmurzin.domain.repositories.TransactionRepository
import kotlinx.coroutines.flow.Flow
import java.util.*
import javax.inject.Inject

class AccountRepositoryImpl @Inject constructor(
    private val localDataSource: AccountLocalDataSource
) : AccountRepository {

    override suspend fun getAccountTypeById(id: Int): AccountType =
        localDataSource.getAccountTypeById(id)

    override suspend fun getAllAccountTypes(): Flow<List<AccountType>> =
        localDataSource.getAllAccountTypes()

    override suspend fun getAccountId(account: AccountType): Int =
        localDataSource.getAccountId(account)

}