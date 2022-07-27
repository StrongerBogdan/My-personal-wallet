package com.bogdanmurzin.domain.usecases.transaction_category

import com.bogdanmurzin.domain.entities.TransactionCategory
import com.bogdanmurzin.domain.repositories.TrxCategoryRepository
import javax.inject.Inject

class GetTrxCategoryIdUseCase @Inject constructor(private val trxCategoryRepository: TrxCategoryRepository) {

    suspend operator fun invoke(trxCategory: TransactionCategory): Int =
        trxCategoryRepository.getTrxCategoryId(trxCategory)
}