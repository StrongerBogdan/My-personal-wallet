package com.bogdanmurzin.data.db

import androidx.room.*
import androidx.room.migration.AutoMigrationSpec
import com.bogdanmurzin.data.entity.AccountTypeEntity
import com.bogdanmurzin.data.entity.TransactionCategoryEntity
import com.bogdanmurzin.data.entity.TransactionEntity

@Database(
    entities = [TransactionEntity::class, AccountTypeEntity::class, TransactionCategoryEntity::class],
    version = 2,
    exportSchema = true,
    autoMigrations = [
        AutoMigration(from = 1, to = 2, spec = AppDatabase.DBAutoMigration::class)
    ]
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun transactionsDao(): TransactionsDao
    abstract fun accountTypeDao(): AccountTypeDao
    abstract fun transactionCategoryDao(): TransactionCategoryDao

    @RenameTable(fromTableName = "transaction_category", toTableName = "trx_category")
    class DBAutoMigration : AutoMigrationSpec
}