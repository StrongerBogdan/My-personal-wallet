package com.bogdanmurzin.data.db

import androidx.room.*
import com.bogdanmurzin.data.entity.AccountTypeEntity
import com.bogdanmurzin.data.entity.TransactionCategoryEntity
import com.bogdanmurzin.data.entity.TransactionEntity
import com.bogdanmurzin.domain.entities.Transaction
import com.bogdanmurzin.domain.entities.TransactionCategory
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionCategoryDao {

    @Query("SELECT * FROM `transaction_category` WHERE id = :id")
    suspend fun getTrxCategoryById(id: Int): TransactionCategoryEntity

    @Query("SELECT * FROM `transaction_category` GROUP BY title")
    fun getAllTrxCategories(): Flow<List<TransactionCategoryEntity>>

    @Query("SELECT * FROM `transaction_category` WHERE title = :title AND transaction_pic_uri = :imageUri")
    fun getTrxCategoryId(title: String, imageUri: String): Int

    @Query("SELECT * FROM `transaction_category` WHERE title = :title AND subcategory IS NOT NULL")
    fun getAllTrxSubCategories(title: String): Flow<List<TransactionCategoryEntity>>

    // IF subcategory is null
    @Query("SELECT * FROM `transaction_category` WHERE title = :title AND  subcategory is NULL")
    fun getTrxCategoryIdBySubcategory(title: String): TransactionCategoryEntity

    // IF subcategory is not null
    @Query("SELECT * FROM `transaction_category` WHERE title = :title AND subcategory = :subcategory")
    fun getTrxCategoryIdBySubcategory(title: String, subcategory: String): TransactionCategoryEntity

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(trxCategory: TransactionCategoryEntity)

    @Query("Update transaction_category SET title = :titleNew WHERE title = :titleOld")
    fun update(titleNew: String, titleOld: String)
}