package com.bogdanmurzin.data.di

import com.bogdanmurzin.data.repositories.TransactionRepositoryImpl
import com.bogdanmurzin.data.repositories.TransactionsLocalDataSource
import com.bogdanmurzin.data.repositories.TransactionsLocalDataSourceImpl
import com.bogdanmurzin.domain.repositories.TransactionRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class TransactionsBindingModule {

    @Binds
    abstract fun bindTransactionRepository(impl: TransactionRepositoryImpl): TransactionRepository

    @Binds
    abstract fun bindTransactionsLocalDataSource(impl: TransactionsLocalDataSourceImpl): TransactionsLocalDataSource
}