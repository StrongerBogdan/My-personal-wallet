package com.bogdanmurzin.domain.entities

import java.util.*

data class Transaction(
    val category: MoneyTransactionCategory,
    val date: Date,
    val description: String?,
    val accountType: AccountType,
    val transactionAmount: Float
)