package com.bogdanmurzin.mypersonalwallet.di

import com.bogdanmurzin.mypersonalwallet.util.CoroutineDispatcherProvider
import com.bogdanmurzin.mypersonalwallet.util.DefaultCoroutineDispatcherProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AppBindModule {

    @Binds
    abstract fun bindCoroutineDispatcherProvider(impl: DefaultCoroutineDispatcherProvider): CoroutineDispatcherProvider
}