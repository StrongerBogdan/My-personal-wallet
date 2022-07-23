package com.bogdanmurzin.data.db

import androidx.room.Dao
import androidx.room.Query
import com.bogdanmurzin.data.entity.AccountTypeEntity
import com.bogdanmurzin.data.entity.TransactionCategoryEntity
import com.bogdanmurzin.data.entity.TransactionEntity
import com.bogdanmurzin.domain.entities.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionCategoryDao {

    @Query("SELECT * FROM `transaction_category` WHERE id = :id")
    fun getTransactionCategory(id: Int): TransactionCategoryEntity
}