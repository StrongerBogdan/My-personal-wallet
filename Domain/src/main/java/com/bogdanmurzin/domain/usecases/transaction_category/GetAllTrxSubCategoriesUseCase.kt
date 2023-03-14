package com.bogdanmurzin.domain.usecases.transaction_category

import com.bogdanmurzin.domain.entities.TransactionCategory
import com.bogdanmurzin.domain.repositories.TrxCategoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

// "trx" it's short form of "transaction"
class GetAllTrxSubCategoriesUseCase @Inject constructor(private val trxCategoryRepository: TrxCategoryRepository) {

    suspend operator fun invoke(title: String): Flow<List<TransactionCategory>> =
        trxCategoryRepository.getAllTrxSubCategories(title)
}