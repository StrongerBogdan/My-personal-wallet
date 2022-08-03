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
            setCategoryTitle(trxCategory.title, trxCategory.subcategory),
            trxCategory.imageUri
        )

    fun toTrxCategory(trxCategoryUi: TrxCategoryUiModel): TransactionCategory =
        TransactionCategory(
            trxCategoryUi.id,
            separateTitle(trxCategoryUi.title),
            separateSubcategory(trxCategoryUi.title),
            trxCategoryUi.imageUri
        )

    private fun setCategoryTitle(title: String, subcategory: String?): String =
        if (subcategory != null) {
            context.getString(
                R.string.category_with_subcategory,
                title,
                subcategory
            )
        } else {
            title
        }

    private fun separateTitle(titleAndSubtitle: String): String {
        return getTitleRegex.find(titleAndSubtitle)?.value ?: EMPTY_STRING
    }

    private fun separateSubcategory(titleAndSubtitle: String): String? {
        return getTitleRegex.find(titleAndSubtitle)?.next()?.value
    }

    // Get first word (Category title)
    // second match
    private val getTitleRegex = """\w+""".toRegex()

    companion object {
        const val EMPTY_STRING = ""
    }
}