package com.bogdanmurzin.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.bogdanmurzin.data.entity.AccountTypeEntity
import com.bogdanmurzin.data.entity.TransactionEntity
import com.bogdanmurzin.domain.entities.AccountType
import com.bogdanmurzin.domain.entities.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountTypeDao {

    @Query("SELECT * FROM `account_type` WHERE id = :id")
    suspend fun getAccountTypeById(id: Int): AccountTypeEntity

    @Query("SELECT * FROM `account_type`")
    fun getAllAccountTypes(): Flow<List<AccountTypeEntity>>

    @Query("SELECT * FROM `account_type` WHERE title = :title AND account_pic_uri = :imageUri")
    fun getAccountId(title: String, imageUri: String): Int

    @Update
    fun update(accountType: AccountTypeEntity)

    @Insert
    suspend fun insert(accountType: AccountTypeEntity)
}