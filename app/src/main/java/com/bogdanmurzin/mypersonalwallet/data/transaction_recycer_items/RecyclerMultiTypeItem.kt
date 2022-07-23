package com.bogdanmurzin.mypersonalwallet.data.transaction_recycer_items

import java.math.BigDecimal

interface RecyclerMultiTypeItem {
    val transactionAmount: Float
    val type: Int
}