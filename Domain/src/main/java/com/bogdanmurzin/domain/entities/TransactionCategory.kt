package com.bogdanmurzin.domain.entities

data class TransactionCategory(
    override val title: String,
    val subcategory: String?,
    override val imageUri: String
) : CategoryEntity