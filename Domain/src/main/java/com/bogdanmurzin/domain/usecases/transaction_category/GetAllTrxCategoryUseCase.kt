package com.bogdanmurzin.domain.usecases.transaction_category

import com.bogdanmurzin.domain.entities.TransactionCategory
import com.bogdanmurzin.domain.repositories.TrxCategoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

// "trx" it's short form of "transaction"
class GetAllTrxCategoryUseCase @Inject constructor(private val trxCategoryRepository: TrxCategoryRepository) {

    suspend operator fun invoke(): Flow<List<TransactionCategory>> =
        trxCategoryRepository.getAllTrxCategories()
}