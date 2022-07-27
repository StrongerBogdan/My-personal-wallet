package com.bogdanmurzin.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transaction_category")
data class TransactionCategoryEntity(
    @PrimaryKey override val id: Int,
    @ColumnInfo(name = "title") override val title: String,
    @ColumnInfo(name = "subcategory") val subcategory: String?,
    @ColumnInfo(name = "transaction_pic_uri") override val imageUri: String
) : CategoryEntity