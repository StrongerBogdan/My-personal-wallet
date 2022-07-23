package com.bogdanmurzin.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import com.bogdanmurzin.domain.entities.MoneyTransactionCategory
import java.util.*

@Entity(
    tableName = "transaction",
    foreignKeys = [ForeignKey(
        entity = TransactionEntity::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("transaction_category_id"),
        onDelete = CASCADE
    ), ForeignKey(
        entity = AccountTypeEntity::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("account_type_id"),
        onDelete = CASCADE
    )]
)
data class TransactionEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "transaction_category_id") val transactionCategoryId: Int,
    @ColumnInfo(name = "date") val date: Date,
    @ColumnInfo(name = "description") val description: String?,
    @ColumnInfo(name = "account_type_id") val accountTypeId: Int,
    @ColumnInfo(name = "transaction_amount") val transactionAmount: Float
)