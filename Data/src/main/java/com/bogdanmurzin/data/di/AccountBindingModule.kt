package com.bogdanmurzin.data.di

import com.bogdanmurzin.data.repositories.account.AccountLocalDataSource
import com.bogdanmurzin.data.repositories.account.AccountLocalDataSourceImpl
import com.bogdanmurzin.data.repositories.account.AccountRepositoryImpl
import com.bogdanmurzin.data.repositories.transaction.TransactionRepositoryImpl
import com.bogdanmurzin.data.repositories.transaction.TransactionsLocalDataSource
import com.bogdanmurzin.data.repositories.transaction.TransactionsLocalDataSourceImpl
import com.bogdanmurzin.domain.repositories.AccountRepository
import com.bogdanmurzin.domain.repositories.TransactionRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AccountBindingModule {

    @Binds
    abstract fun bindAccountRepository(impl: AccountRepositoryImpl): AccountRepository

    @Binds
    abstract fun bindAccountLocalDataSource(impl: AccountLocalDataSourceImpl): AccountLocalDataSource
}