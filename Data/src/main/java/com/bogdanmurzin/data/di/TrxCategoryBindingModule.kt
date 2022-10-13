package com.bogdanmurzin.data.di

import com.bogdanmurzin.data.repositories.transaction_category.TrxCategoryLocalDataSource
import com.bogdanmurzin.data.repositories.transaction_category.TrxCategoryLocalDataSourceImpl
import com.bogdanmurzin.data.repositories.transaction_category.TrxCategoryRepositoryImpl
import com.bogdanmurzin.domain.repositories.TrxCategoryRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class TrxCategoryBindingModule {

    @Binds
    abstract fun bindTrxCategoryRepository(impl: TrxCategoryRepositoryImpl): TrxCategoryRepository

    @Binds
    abstract fun bindTrxCategoryLocalDataSource(impl: TrxCategoryLocalDataSourceImpl)
            : TrxCategoryLocalDataSource
}