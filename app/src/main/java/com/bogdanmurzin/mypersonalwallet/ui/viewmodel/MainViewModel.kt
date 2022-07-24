package com.bogdanmurzin.mypersonalwallet.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bogdanmurzin.domain.usecases.GetTransactionsUseCase
import com.bogdanmurzin.mypersonalwallet.data.transaction_recycer_items.TransactionItemUiModel
import com.bogdanmurzin.mypersonalwallet.mapper.TransactionUiMapper
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MainViewModel @Inject constructor(
    val getTransactionsUseCase: GetTransactionsUseCase,
    val transactionUiMapper: TransactionUiMapper
) : ViewModel() {

    suspend fun updateRateList(): Flow<List<TransactionItemUiModel>> =
        transactionUiMapper.toTransactionUiModel(
            getTransactionsUseCase.invoke()
        )

    @Suppress("UNCHECKED_CAST")
    class Factory(
        private val getTransactionsUseCase: GetTransactionsUseCase,
        private val transactionUiMapper: TransactionUiMapper
    ) : ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            MainViewModel(getTransactionsUseCase, transactionUiMapper) as T
    }
}