package com.bogdanmurzin.domain.entities

data class MoneyTransactionCategory(
    val title: String,
    val subcategory: String?,
    val transactionPicUri: String?
)