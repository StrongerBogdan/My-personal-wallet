package com.bogdanmurzin.domain.usecases.transaction_category

import com.bogdanmurzin.domain.entities.TransactionCategory
import com.bogdanmurzin.domain.repositories.TrxCategoryRepository
import javax.inject.Inject

class UpdateTrxCategoryUseCase @Inject constructor(private val trxCategoryRepository: TrxCategoryRepository) {

    suspend operator fun invoke(trxCategory: TransactionCategory) =
        trxCategoryRepository.updateTrxCategory(trxCategory)
}