package com.bogdanmurzin.domain.usecases.transaction_category

import com.bogdanmurzin.domain.repositories.TrxCategoryRepository
import javax.inject.Inject

class DeleteTrxCategoryUseCase @Inject constructor(private val trxCategoryRepository: TrxCategoryRepository) {

    suspend operator fun invoke(id: Int) =
        trxCategoryRepository.deleteTrxCategory(id)
}