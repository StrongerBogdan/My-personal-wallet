package com.bogdanmurzin.data.repositories.account

import com.bogdanmurzin.data.db.AccountTypeDao
import com.bogdanmurzin.data.mapper.AccountTypeEntityMapper
import com.bogdanmurzin.domain.entities.AccountType
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AccountLocalDataSourceImpl @Inject constructor(
    private val accountTypeDao: AccountTypeDao,
    private val dispatcher: CoroutineDispatcher,
    private val accountTypeEntityMapper: AccountTypeEntityMapper
) : AccountLocalDataSource {

    override suspend fun getAccountTypeById(id: Int): AccountType =
        withContext(dispatcher) {
            val result = accountTypeDao.getAccountTypeById(id)
            accountTypeEntityMapper.toAccountType(result)
        }
}