package com.bogdanmurzin.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "account_type",
    indices = [Index(
        value = ["title", "account_pic_uri"],
        unique = true
    )]
)
data class AccountTypeEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "account_pic_uri") val imageUri: String
)