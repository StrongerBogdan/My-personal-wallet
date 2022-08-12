package com.bogdanmurzin.data.db

import androidx.room.Database
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
}