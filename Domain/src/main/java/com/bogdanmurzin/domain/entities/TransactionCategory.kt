package com.bogdanmurzin.domain.entities

data class TransactionCategory(
    override val id: Int,
    override val title: String,
    val subcategory: String?,
    override val imageUri: String
) : CategoryEntity