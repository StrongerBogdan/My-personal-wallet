package com.bogdanmurzin.data.db

import androidx.room.Dao
import androidx.room.Query
import com.bogdanmurzin.data.entity.TransactionEntity
import com.bogdanmurzin.domain.entities.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionsDao {

    @Query("SELECT * FROM `transaction`")
    fun getAll(): Flow<List<TransactionEntity>>
}