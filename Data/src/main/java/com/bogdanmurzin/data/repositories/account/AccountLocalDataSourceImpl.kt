package com.bogdanmurzin.data.repositories.account

import com.bogdanmurzin.data.db.AccountTypeDao
import com.bogdanmurzin.data.mapper.AccountTypeEntityMapper
import com.bogdanmurzin.domain.entities.AccountType
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
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
            return@withContext accountTypeEntityMapper.toAccountType(result)
        }

    override suspend fun getAllAccountTypes(): Flow<List<AccountType>> =
        withContext(dispatcher) {
            val flow = accountTypeDao.getAllAccountTypes()
            return@withContext flow.map { list ->
                list.map { item ->
                    accountTypeEntityMapper.toAccountType(item)
                }
            }
        }

    override suspend fun getAccountId(account: AccountType): Int =
        withContext(dispatcher) {
            accountTypeDao.getAccountId(account.title, account.imageUri)
        }

    override suspend fun updateAccount(account: AccountType) =
        withContext(dispatcher) {
            accountTypeDao.update(
                accountTypeEntityMapper.toAccountEntity(account)
            )
        }

    override suspend fun insertAccount(account: AccountType) =
        withContext(dispatcher) {
            accountTypeDao.insert(
                accountTypeEntityMapper.toAccountEntity(account)
            )
        }
}