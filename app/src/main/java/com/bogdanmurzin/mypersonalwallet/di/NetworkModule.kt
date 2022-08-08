package com.bogdanmurzin.mypersonalwallet.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    fun provideLoggerInterceptor(): Interceptor =
        HttpLoggingInterceptor().apply {
            HttpLoggingInterceptor().level = HttpLoggingInterceptor.Level.BASIC
        }

    @Provides
    fun provideGsonConvertorFactory(): GsonConverterFactory =
        GsonConverterFactory.create()

    @Provides
    fun provideRxJava3CallAdapterFactory(): RxJava3CallAdapterFactory =
        RxJava3CallAdapterFactory.create()
}