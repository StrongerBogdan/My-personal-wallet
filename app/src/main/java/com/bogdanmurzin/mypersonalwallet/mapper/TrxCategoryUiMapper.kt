package com.bogdanmurzin.mypersonalwallet.mapper

import android.content.Context
import com.bogdanmurzin.domain.entities.TransactionCategory
import com.bogdanmurzin.mypersonalwallet.R
import com.bogdanmurzin.mypersonalwallet.data.TrxCategoryUiModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class TrxCategoryUiMapper @Inject constructor(@ApplicationContext val context: Context) {

    fun toTrxCategoryUiModel(trxCategory: TransactionCategory): TrxCategoryUiModel =
        TrxCategoryUiModel(
            trxCategory.id,
            trxCategory.title,
            trxCategory.subcategory,
            setCompositeTitle(trxCategory.title, trxCategory.subcategory),
            trxCategory.imageUri
        )

    fun toTrxCategory(trxCategoryUi: TrxCategoryUiModel): TransactionCategory =
        TransactionCategory(
            trxCategoryUi.id,
            trxCategoryUi.title,
            trxCategoryUi.subcategory,
            trxCategoryUi.imageUri
        )

    private fun setCompositeTitle(title: String, subcategory: String?): String =
        if (subcategory != null) {
            context.getString(
                R.string.category_with_subcategory,
                title,
                subcategory
            )
        } else {
            title
        }
}