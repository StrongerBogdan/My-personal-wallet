package com.bogdanmurzin.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.bogdanmurzin.data.entity.AccountTypeEntity
import com.bogdanmurzin.data.entity.TransactionCategoryEntity
import com.bogdanmurzin.data.entity.TransactionEntity

@Database(
    entities = [TransactionEntity::class, AccountTypeEntity::class, TransactionCategoryEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun transactionsDao(): TransactionsDao
    abstract fun accountTypeDao(): AccountTypeDao
    abstract fun transactionCategoryDao(): TransactionCategoryDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    AppDatabase::class.simpleName!!
                // TODO: try do not use it, all API and DB calls should be in background thread to prevent UI layer from dropping frames
                )
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}