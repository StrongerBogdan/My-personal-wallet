package com.bogdanmurzin.data.mapper

import com.bogdanmurzin.data.entity.AccountTypeEntity
import com.bogdanmurzin.domain.entities.AccountType
import javax.inject.Inject

class AccountTypeEntityMapper @Inject constructor() {

    fun toAccountType(item: AccountTypeEntity): AccountType =
        AccountType(item.title, item.imageUri)
}