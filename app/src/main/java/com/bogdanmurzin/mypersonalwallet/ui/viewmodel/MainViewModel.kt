package com.bogdanmurzin.mypersonalwallet.ui.viewmodel

import androidx.lifecycle.*
import androidx.navigation.NavDirections
import com.bogdanmurzin.domain.usecases.transaction.DeleteTransactionsUseCase
import com.bogdanmurzin.domain.usecases.transaction.GetTransactionsUseCase
import com.bogdanmurzin.mypersonalwallet.data.transaction_recycer_items.TransactionItemUiModel
import com.bogdanmurzin.mypersonalwallet.mapper.TransactionUiMapper
import com.bogdanmurzin.mypersonalwallet.ui.fragment.FragmentMoneyTransactionsDirections
import com.bogdanmurzin.mypersonalwallet.util.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getTransactionsUseCase: GetTransactionsUseCase,
    private val deleteTransactionsUseCase: DeleteTransactionsUseCase,
    private val transactionUiMapper: TransactionUiMapper
) : ViewModel() {

    private val _transactionList: MutableLiveData<List<TransactionItemUiModel>> = MutableLiveData()
    var transactionsList: LiveData<List<TransactionItemUiModel>> = _transactionList
    private val _action: SingleLiveEvent<NavDirections> = SingleLiveEvent()
    val action: LiveData<NavDirections> = _action

    var isDeleteEnabled = false
    var selectedTransactionsIds = listOf<Int>()

    fun updateTransactions() {
        viewModelScope.launch {
            getTransactionList()
        }
    }

    private suspend fun getTransactionList() {
        return getTransactionsUseCase.invoke()
            .map { list ->
                transactionUiMapper.toListOfTransactionUiModel(list, selectedTransactionsIds)
            }
            .collect { _transactionList.postValue(it) }
    }

    fun openBottomSheet(event: Event) {
        if (event is Event.OpenPreviewScreen) {
            _action.postValue(
                FragmentMoneyTransactionsDirections
                    .actionFragmentMoneyTransactionsToBottomSheetAddTransaction(event.id)
            )
        }
    }

    fun deleteTransactions(transactionIds: List<Int>) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteTransactionsUseCase.invoke(transactionIds)
        }
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(
        private val getTransactionsUseCase: GetTransactionsUseCase,
        private val deleteTransactionsUseCase: DeleteTransactionsUseCase,
        private val transactionUiMapper: TransactionUiMapper
    ) : ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            MainViewModel(
                getTransactionsUseCase,
                deleteTransactionsUseCase,
                transactionUiMapper
            ) as T
    }
}