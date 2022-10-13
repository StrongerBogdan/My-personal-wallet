package com.bogdanmurzin.mypersonalwallet.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.bogdanmurzin.data.db.AppDatabase
import com.bogdanmurzin.mypersonalwallet.common.Constants.PREF_THEME_COLOR
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.util.*

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Volatile
    private var INSTANCE: AppDatabase? = null

    @Provides
    fun provideRatesDatabase(@ApplicationContext context: Context): AppDatabase =
        INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                AppDatabase::class.simpleName!!
            )
                .build()
            INSTANCE = instance
            // return instance
            instance
        }

    @Provides
    fun provideSharedPreference(@ApplicationContext context: Context): SharedPreferences =
        context.getSharedPreferences(PREF_THEME_COLOR, Context.MODE_PRIVATE)

    @Provides
    fun provideLocale(): Locale = Locale.getDefault()

}