package com.bogdanmurzin.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.bogdanmurzin.data.entity.TransactionEntity
import com.bogdanmurzin.domain.entities.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionsDao {

    @Query("SELECT * FROM `transaction`")
    fun getAll(): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM `transaction` WHERE id = :id")
    fun getById(id: Int): TransactionEntity

    @Update
    fun update(transaction: TransactionEntity)

    @Insert
    suspend fun insert(transaction: TransactionEntity)
}