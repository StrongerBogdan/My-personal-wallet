package com.bogdanmurzin.domain.usecases.transaction_category

import com.bogdanmurzin.domain.repositories.TrxCategoryRepository
import javax.inject.Inject

class GetTrxCategoryIdBySubcategoryUseCase @Inject constructor(private val trxCategoryRepository: TrxCategoryRepository) {

    suspend operator fun invoke(title: String, subcategory: String?): Int =
        trxCategoryRepository.getTrxCategoryIdBySubcategory(title, subcategory)
}