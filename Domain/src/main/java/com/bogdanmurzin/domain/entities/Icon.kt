package com.bogdanmurzin.domain.entities

data class Icon(
    val iconId: Int,
    val preview: String,
    val download: String,
    val isPaid: Boolean
)