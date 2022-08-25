package com.bogdanmurzin.mypersonalwallet.ui.viewmodel

import androidx.lifecycle.*
import com.bogdanmurzin.domain.usecases.transaction.DeleteTransactionsUseCase
import com.bogdanmurzin.domain.usecases.transaction.GetTransactionsUseCase
import com.bogdanmurzin.mypersonalwallet.data.transaction_recycer_items.RecyclerMultiTypeItem
import com.bogdanmurzin.mypersonalwallet.data.transaction_recycer_items.TransactionItemUiModel
import com.bogdanmurzin.mypersonalwallet.mapper.HeaderItemUiMapper
import com.bogdanmurzin.mypersonalwallet.mapper.TransactionUiMapper
import com.bogdanmurzin.mypersonalwallet.util.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getTransactionsUseCase: GetTransactionsUseCase,
    private val deleteTransactionsUseCase: DeleteTransactionsUseCase,
    private val transactionUiMapper: TransactionUiMapper,
    private val headerItemUiMapper: HeaderItemUiMapper
) : ViewModel() {

    private val _transactionList: MutableLiveData<List<RecyclerMultiTypeItem>> = MutableLiveData()
    var transactionsList: LiveData<List<RecyclerMultiTypeItem>> = _transactionList
    private val _action: SingleLiveEvent<Event> = SingleLiveEvent()
    val action: LiveData<Event> = _action

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
            .collect { _transactionList.postValue(addHeaders(it)) }
    }

    fun openBottomSheet(id: Int) {
        _action.postValue(Event.OpenPreviewScreen(id))
    }

    fun deleteTransactions(transactionIds: List<Int>) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteTransactionsUseCase.invoke(transactionIds)
        }
    }

    fun startSettingsActivity() {
        _action.postValue(Event.OpenSettingsActivity)
    }

    private fun addHeaders(transactionList: List<TransactionItemUiModel>): List<RecyclerMultiTypeItem> {
        val customComparator =
            compareBy<Date> { it.year }.thenBy { it.month }.thenBy { it.day }
        val resultList = mutableListOf<RecyclerMultiTypeItem>()
        var previousDate: Date? = null
        transactionList.forEach { transaction ->
            val date = transaction.date
            if (previousDate == null) {
                // Add very first header
                resultList.add(
                    headerItemUiMapper.toHeaderItemUiMapper(date, 0f)
                )
                // save last date
                previousDate = date
            }
            // If our header date != transaction date -> add new header
            if (customComparator.compare(date, previousDate) != 0) {
                resultList.add(
                    headerItemUiMapper.toHeaderItemUiMapper(date, 0f)
                )
                previousDate = date
            }
            // Add transaction
            resultList.add(transaction)
        }
        return resultList
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(
        private val getTransactionsUseCase: GetTransactionsUseCase,
        private val deleteTransactionsUseCase: DeleteTransactionsUseCase,
        private val transactionUiMapper: TransactionUiMapper,
        private val headerItemUiMapper: HeaderItemUiMapper
    ) : ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            MainViewModel(
                getTransactionsUseCase,
                deleteTransactionsUseCase,
                transactionUiMapper,
                headerItemUiMapper
            ) as T
    }
}