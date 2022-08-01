package com.bogdanmurzin.domain.entities

import java.util.*

data class Transaction(
    val id: Int,
    val category: TransactionCategory,
    val date: Date,
    val description: String?,
    val accountType: AccountType,
    val transactionAmount: Float
)