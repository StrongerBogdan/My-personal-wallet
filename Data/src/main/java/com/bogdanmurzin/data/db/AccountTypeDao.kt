package com.bogdanmurzin.data.db

import androidx.room.Dao
import androidx.room.Query
import com.bogdanmurzin.data.entity.AccountTypeEntity
import com.bogdanmurzin.data.entity.TransactionEntity
import com.bogdanmurzin.domain.entities.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountTypeDao {

    @Query("SELECT * FROM `account_type` WHERE id = :id")
    fun getAccountType(id: Int): AccountTypeEntity
}