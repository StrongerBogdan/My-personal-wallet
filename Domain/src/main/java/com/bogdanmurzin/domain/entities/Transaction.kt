package com.bogdanmurzin.domain.entities

import java.util.*

data class Transaction(
    val category: TransactionCategory,
    val date: Date,
    val description: String?,
    val accountType: AccountType,
    val transactionAmount: Float
)