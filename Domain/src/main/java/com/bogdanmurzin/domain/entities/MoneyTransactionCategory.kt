package com.bogdanmurzin.domain.entities

data class MoneyTransactionCategory(
    override val title: String,
    val subcategory: String?,
    override val imageUri: String
) : CategoryEntity