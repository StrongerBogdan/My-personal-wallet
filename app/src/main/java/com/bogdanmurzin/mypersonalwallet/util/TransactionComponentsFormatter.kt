package com.bogdanmurzin.mypersonalwallet.util

import com.bogdanmurzin.mypersonalwallet.common.Constants
import javax.inject.Inject

class TransactionComponentsFormatter @Inject constructor() {

    fun formatTransactionAmount(rawTransactionAmount: String): String =
        rawTransactionAmount.replace(Constants.DOLLAR_OR_COMA_REGEX, Constants.EMPTY_STRING)

    fun formatDescription(rawDescription: String): String? =
        rawDescription.ifEmpty { null }
}