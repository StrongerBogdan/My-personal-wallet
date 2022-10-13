package com.bogdanmurzin.mypersonalwallet

import com.bogdanmurzin.mypersonalwallet.util.TransactionComponentsFormatter
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class TransactionComponentsFormatterTest {

    lateinit var formatter: TransactionComponentsFormatter

    @Before
    fun before() {
        formatter = TransactionComponentsFormatter()
    }

    @Test
    fun formatTransactionAmount_Format() {
        // Arrange
        // -

        // Act
        val result = formatter.formatTransactionAmount("$34,235.55")

        // Assert
        assertEquals("34235.55", result)
    }

    @Test
    fun formatDescription_NotEmptyString_returnsEqualString() {
        // Arrange
        // -

        // Act
        val result = formatter.formatDescription("Blah-blah-Blah")

        // Assert
        assertEquals("Blah-blah-Blah", result)
    }

    @Test
    fun formatDescription_EmptyString_returnsNull() {
        // Arrange
        // -

        // Act
        val result = formatter.formatDescription("")

        // Assert
        assertEquals(null, result)
    }

}