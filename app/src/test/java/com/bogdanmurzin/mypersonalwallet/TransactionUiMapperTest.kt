package com.bogdanmurzin.mypersonalwallet

import com.bogdanmurzin.domain.entities.AccountType
import com.bogdanmurzin.domain.entities.Transaction
import com.bogdanmurzin.domain.entities.TransactionCategory
import com.bogdanmurzin.mypersonalwallet.data.TrxCategoryUiModel
import com.bogdanmurzin.mypersonalwallet.mapper.TransactionUiMapper
import com.bogdanmurzin.mypersonalwallet.mapper.TrxCategoryUiMapper
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.util.*

class TransactionUiMapperTest {

    private lateinit var mapper: TransactionUiMapper
    private lateinit var trxCategoryUiMapper: TrxCategoryUiMapper

    @Before
    fun before() {
        trxCategoryUiMapper = mockk()
        every { trxCategoryUiMapper.toTrxCategoryUiModel(any()) } returns createTrxCategoryUiModelDummy()
        mapper = TransactionUiMapper(trxCategoryUiMapper, Locale.US)
    }

    private fun createTrxCategoryUiModelDummy(): TrxCategoryUiModel = mockk(relaxed = true)
    private fun createTrxCategoryDummy(): TransactionCategory = mockk(relaxed = true)
    private fun createAccountTypeDummy(): AccountType = mockk(relaxed = true)
    private fun createDate(plusDays: Int): Date {
        //  GMT: Friday, April 13, 2007 8:54:49 PM
        return Date(1176497689000 + DAY_IN_MILS * plusDays)
    }

    private fun createTransaction(id: Int = 1, plusDays: Int = 0): Transaction =
        Transaction(
            id,
            createTrxCategoryDummy(),
            createDate(plusDays),
            "Blah-blah-blah",
            createAccountTypeDummy(),
            133.7f
        )

    @Test
    fun toListOfTransactionUiModel_Add2Transactions_Map() {
        // Arrange
        val list = listOf(createTransaction(1), createTransaction(2))

        // Act
        val result = mapper.toListOfTransactionUiModel(list)

        // Assert
        verify(exactly = 2) { trxCategoryUiMapper.toTrxCategoryUiModel(any()) }
        assertEquals(1, result[0].id)
        assertEquals(2, result[1].id)
        assertEquals(133.7f, result[0].transactionAmount)
        assertEquals(133.7f, result[1].transactionAmount)
    }

    @Test
    fun toListOfTransactionUiModel_Add3TransactionsAndChecksOrderByDate_Map() {
        // Arrange
        val list = listOf(createTransaction(1, 1), createTransaction(2, 3), createTransaction(3, 2))

        // Act
        val result = mapper.toListOfTransactionUiModel(list)

        // Assert
        verify(exactly = 3) { trxCategoryUiMapper.toTrxCategoryUiModel(any()) }
        assertEquals(2, result[0].id)
        assertEquals(3, result[1].id)
        assertEquals(1, result[2].id)
    }

    @Test
    fun toListOfTransactionUiModel_Add6TransactionsAndChecksSelectedList_Map() {
        // Arrange
        val list = listOf(
            createTransaction(1), createTransaction(2), createTransaction(3),
            createTransaction(4), createTransaction(5), createTransaction(6)
        )
        val selectedList = listOf(1, 3, 4, 5)

        // Act
        val result = mapper.toListOfTransactionUiModel(list, selectedList)

        // Assert
        verify(exactly = 6) { trxCategoryUiMapper.toTrxCategoryUiModel(any()) }
        result.forEach {
            if (selectedList.contains(it.id)){
                assertTrue(it.isSelected)
            } else {
                assertFalse(it.isSelected)
            }
        }
    }

    @Test
    fun toTransactionUiModel_Map() {
        // Arrange
        val transaction = createTransaction(1)

        // Act
        val result = mapper.toTransactionUiModel(transaction)

        // Assert
        assertEquals(1, result.id)
        assertEquals("$133.70", result.transactionAmount)

    }


    companion object {
        const val DAY_IN_MILS = 86400000
    }
}