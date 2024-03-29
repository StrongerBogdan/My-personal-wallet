package com.bogdanmurzin.data.mapper

import com.bogdanmurzin.data.entity.AccountTypeEntity
import com.bogdanmurzin.data.entity.TransactionCategoryEntity
import com.bogdanmurzin.data.entity.TransactionEntity
import com.bogdanmurzin.domain.entities.Transaction
import javax.inject.Inject

class TransactionsEntityMapper @Inject constructor(
    private val accountTypeEntityMapper: AccountTypeEntityMapper,
    private val transactionCategoryEntityMapper: TransactionCategoryEntityMapper
) {

    fun toTransaction(
        item: TransactionEntity,
        transactionCategoryEntity: TransactionCategoryEntity,
        accountTypeEntity: AccountTypeEntity
    ): Transaction =
        Transaction(
            item.id,
            transactionCategoryEntityMapper.toTransactionCategory(transactionCategoryEntity),
            item.date,
            item.description,
            accountTypeEntityMapper.toAccountType(accountTypeEntity),
            item.transactionAmount
        )

    fun toTransactionEntity(
        transaction: Transaction,
        trxCategoryId: Int,
        accountTypeId: Int
    ): TransactionEntity =
        TransactionEntity(
            transaction.id,
            trxCategoryId,
            transaction.date,
            transaction.description,
            accountTypeId,
            transaction.transactionAmount
        )
}