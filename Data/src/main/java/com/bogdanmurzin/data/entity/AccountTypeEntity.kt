package com.bogdanmurzin.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "account_type")
data class AccountTypeEntity(
    @PrimaryKey override val id: Int,
    @ColumnInfo(name = "title") override val title: String,
    @ColumnInfo(name = "account_pic_uri") override val imageUri: String
) : CategoryEntity