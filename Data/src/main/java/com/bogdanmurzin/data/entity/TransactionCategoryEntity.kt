package com.bogdanmurzin.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "trx_category",
    indices = [Index(
        value = ["title", "subcategory", "transaction_pic_uri"],
        unique = true
    )]
)
data class TransactionCategoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "subcategory") val subcategory: String?,
    @ColumnInfo(name = "transaction_pic_uri") val imageUri: String
)