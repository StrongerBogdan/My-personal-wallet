package com.bogdanmurzin.mypersonalwallet.ui.viewmodel

import androidx.lifecycle.*
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.bogdanmurzin.domain.usecases.transaction.GetTransactionsUseCase
import com.bogdanmurzin.mypersonalwallet.R
import com.bogdanmurzin.mypersonalwallet.data.transaction_recycer_items.TransactionItemUiModel
import com.bogdanmurzin.mypersonalwallet.mapper.TransactionUiMapper
import com.bogdanmurzin.mypersonalwallet.ui.fragment.FragmentMoneyTransactionsDirections
import com.bogdanmurzin.mypersonalwallet.util.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getTransactionsUseCase: GetTransactionsUseCase,
    private val transactionUiMapper: TransactionUiMapper
) : ViewModel() {

    private val _transactionList: MutableLiveData<List<TransactionItemUiModel>> = MutableLiveData()
    var transactionsList: LiveData<List<TransactionItemUiModel>> = _transactionList
    private val _action: SingleLiveEvent<NavDirections> = SingleLiveEvent()
    val action: SingleLiveEvent<NavDirections> = _action


    init {
        viewModelScope.launch {
            getTransactionList()
        }
    }

    private suspend fun getTransactionList() {
        return getTransactionsUseCase.invoke()
            .map { list -> transactionUiMapper.toListOfTransactionUiModel(list) }
            .collect { _transactionList.postValue(it) }
    }

    fun openBottomSheet(event: Event) {
        if (event is Event.OpenPreviewScreen) {
            action.postValue(
                FragmentMoneyTransactionsDirections
                    .actionFragmentMoneyTransactionsToBottomSheetAddTransaction(event.id)
            )
        }
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(
        private val getTransactionsUseCase: GetTransactionsUseCase,
        private val transactionUiMapper: TransactionUiMapper
    ) : ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            MainViewModel(getTransactionsUseCase, transactionUiMapper) as T
    }
}