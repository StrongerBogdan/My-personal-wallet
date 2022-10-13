package com.bogdanmurzin.mypersonalwallet.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.bogdanmurzin.domain.entities.Transaction
import com.bogdanmurzin.domain.repositories.TransactionRepository
import com.bogdanmurzin.domain.usecases.transaction.DeleteTransactionsUseCase
import com.bogdanmurzin.domain.usecases.transaction.GetTransactionsUseCase
import com.bogdanmurzin.mypersonalwallet.MainCoroutineRule
import com.bogdanmurzin.mypersonalwallet.TransactionUiMapperTest
import com.bogdanmurzin.mypersonalwallet.data.transaction_recycer_items.TransactionItemUiModel
import com.bogdanmurzin.mypersonalwallet.mapper.HeaderItemUiMapper
import com.bogdanmurzin.mypersonalwallet.mapper.TransactionUiMapper
import com.bogdanmurzin.mypersonalwallet.util.Event
import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import java.util.*

class MainViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    var mainRule = MainCoroutineRule()

    @MockK
    lateinit var transactionRepo: TransactionRepository

    @InjectMockKs
    lateinit var getTransactionsUseCase: GetTransactionsUseCase

    @RelaxedMockK
    lateinit var deleteTransactionsUseCase: DeleteTransactionsUseCase

    @RelaxedMockK
    lateinit var transactionUiMapper: TransactionUiMapper

    @MockK
    lateinit var headerItemUiMapper: HeaderItemUiMapper

    @RelaxedMockK
    lateinit var observer: Observer<List<TransactionItemUiModel>>

    @RelaxedMockK
    lateinit var observerEvent: Observer<Event>

    private lateinit var viewModel: MainViewModel


    @Before
    fun setUp() {
        // Init mocks
        MockKAnnotations.init(this)
        // return fake data
        coEvery { transactionRepo.getLocalTransactions() } returns flow {
            emit(
                createListOfTransactions()
            )
        }
        // init viewModel
        viewModel = MainViewModel(
            getTransactionsUseCase,
            deleteTransactionsUseCase,
            transactionUiMapper,
            headerItemUiMapper
        )
        // Add observer
        viewModel.transactionsList.observeForever(observer)
        viewModel.action.observeForever(observerEvent)
    }


    @Test
    fun getTransactionsList() {
        // Arrange
        val expected: List<Transaction> = createListOfTransactions()

        // Act
        viewModel.updateTransactions()

        // Assert
        coVerifySequence {
            transactionRepo.getLocalTransactions()
            observer.onChanged(any())
        }
        assertEquals(expected, viewModel.transactionsList.value)
    }

    @Test
    fun deleteTransactions() {
        // Arrange
        // -

        // Act
        viewModel.deleteTransactions(listOf())

        // Assert
        coVerify(exactly = 1) { deleteTransactionsUseCase.invoke(any()) }
    }

    @Test
    fun openBottomSheet() {
        // Arrange
        val id = 25

        // Act
        viewModel.openBottomSheet(id)

        // Assert
        coVerify(exactly = 1) { observerEvent.onChanged(any()) }
        assertTrue(viewModel.action.value is Event.OpenPreviewScreen)
        assertEquals(
            id,
            (viewModel.action.value as Event.OpenPreviewScreen).id
        )
    }

    @Test
    fun startSettingsActivity() {
        // Arrange
        // -

        // Act
        viewModel.startSettingsActivity()

        // Assert
        coVerify(exactly = 1) { observerEvent.onChanged(any()) }
        assertTrue(viewModel.action.value is Event.OpenSettingsActivity)
    }

    private fun createListOfTransactions(): List<Transaction> {
        val list = mutableListOf<Transaction>()
        for (i in 1..10) {
            createTransaction(i)
        }
        return list
    }

    private fun createTransaction(id: Int): Transaction =
        Transaction(
            id,
            mockk(),
            mockk(),
            "Blah-blah-blah $id",
            mockk(),
            133.7f
        )
}