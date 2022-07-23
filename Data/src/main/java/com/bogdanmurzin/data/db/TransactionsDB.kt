package com.bogdanmurzin.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.bogdanmurzin.data.entity.AccountTypeEntity
import com.bogdanmurzin.data.entity.TransactionCategoryEntity

@Database(
    entities = [TransactionCategoryEntity::class, AccountTypeEntity::class, TransactionCategoryEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class TransactionsDB : RoomDatabase() {
    abstract fun transactionsDao(): TransactionsDao
    abstract fun accountTypeDao(): AccountTypeDao
    abstract fun transactionCategoryDao(): TransactionCategoryDao

    companion object {
        @Volatile
        private var INSTANCE: TransactionsDB? = null

        fun getDatabase(context: Context): TransactionsDB {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TransactionsDB::class.java,
                    TransactionsDB::class.simpleName!!
                ).allowMainThreadQueries()
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}