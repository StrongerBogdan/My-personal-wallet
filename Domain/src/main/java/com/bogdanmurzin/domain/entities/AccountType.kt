package com.bogdanmurzin.domain.entities

data class AccountType(
    override val id: Int,
    override val title: String,
    override val imageUri: String
) : CategoryEntity
