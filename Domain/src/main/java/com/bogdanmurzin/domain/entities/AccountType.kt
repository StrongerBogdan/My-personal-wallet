package com.bogdanmurzin.domain.entities

data class AccountType(
    override val title: String,
    override val imageUri: String?
) : EntityWithImageAndTitle
