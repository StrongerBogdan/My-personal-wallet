package com.bogdanmurzin.mypersonalwallet.di

import android.content.Context
import com.bogdanmurzin.data.db.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideRatesDatabase(@ApplicationContext context: Context): AppDatabase =
        AppDatabase.getDatabase(context)
}