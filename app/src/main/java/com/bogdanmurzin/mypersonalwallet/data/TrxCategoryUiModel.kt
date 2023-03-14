package com.bogdanmurzin.mypersonalwallet.data

// compositeTitle is for mapping title and subtitle like
// if category has subtitle -> "Title(Subtitle)"
// else "Title"
data class TrxCategoryUiModel(
    val id: Int,
    val title: String,
    val subcategory: String?,
    val compositeTitle: String,
    val imageUri: String
)