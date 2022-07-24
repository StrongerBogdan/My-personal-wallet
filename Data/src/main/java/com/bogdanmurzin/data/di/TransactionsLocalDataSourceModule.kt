package com.bogdanmurzin.data.di

import com.bogdanmurzin.data.db.AccountTypeDao
import com.bogdanmurzin.data.db.AppDatabase
import com.bogdanmurzin.data.db.TransactionCategoryDao
import com.bogdanmurzin.data.db.TransactionsDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
@InstallIn(SingletonComponent::class)
class TransactionsLocalDataSourceModule {

    @Provides
    fun provideTransactionsDao(database: AppDatabase): TransactionsDao =
        database.transactionsDao()

    @Provides
    fun provideAccountTypeDao(database: AppDatabase): AccountTypeDao =
        database.accountTypeDao()

    @Provides
    fun provideTransactionCategoryDao(database: AppDatabase): TransactionCategoryDao =
        database.transactionCategoryDao()

    @Provides
    fun provideCoroutineDispatcher(): CoroutineDispatcher =
        Dispatchers.IO
}