package com.bogdanmurzin.mypersonalwallet.ui.viewmodel

import androidx.lifecycle.*
import com.bogdanmurzin.domain.entities.AccountType
import com.bogdanmurzin.domain.entities.Transaction
import com.bogdanmurzin.domain.usecases.account_type.GetAccountTypeUseCase
import com.bogdanmurzin.domain.usecases.transaction.GetTransactionByIdUseCase
import com.bogdanmurzin.domain.usecases.transaction.InsertTransactionUseCase
import com.bogdanmurzin.domain.usecases.transaction.UpdateTransactionUseCase
import com.bogdanmurzin.domain.usecases.transaction_category.GetTrxCategoryUseCase
import com.bogdanmurzin.mypersonalwallet.data.TrxCategoryUiModel
import com.bogdanmurzin.mypersonalwallet.data.transaction_recycer_items.TransactionItemUiModel
import com.bogdanmurzin.mypersonalwallet.mapper.TransactionUiMapper
import com.bogdanmurzin.mypersonalwallet.mapper.TrxCategoryUiMapper
import com.bogdanmurzin.mypersonalwallet.util.EditingState
import com.bogdanmurzin.mypersonalwallet.util.TransactionComponentsFormatter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AddTransactionViewModel @Inject constructor(
    private val getAccountTypeUseCase: GetAccountTypeUseCase,
    private val getTrxCategoryUseCase: GetTrxCategoryUseCase,

    private val insertTransactionUseCase: InsertTransactionUseCase,
    private val getTransactionByIdUseCase: GetTransactionByIdUseCase,
    private val updateTransactionUseCase: UpdateTransactionUseCase,

    private val transactionUiMapper: TransactionUiMapper,
    private val trxCategoryUiMapper: TrxCategoryUiMapper,
    private val transactionComponentsFormatter: TransactionComponentsFormatter
) : ViewModel() {

    private val _selectedAccountType: MutableLiveData<AccountType> = MutableLiveData()
    val selectedAccountType: LiveData<AccountType> = _selectedAccountType
    private val _selectedTrxCategory: MutableLiveData<TrxCategoryUiModel> = MutableLiveData()
    val selectedTrxCategory: LiveData<TrxCategoryUiModel> = _selectedTrxCategory
    private val _selectedDate: MutableLiveData<Date> = MutableLiveData(Calendar.getInstance().time)
    val selectedDate: LiveData<Date> = _selectedDate
    private val _loadedTransaction: MutableLiveData<TransactionItemUiModel> = MutableLiveData()
    var loadedTransaction: LiveData<TransactionItemUiModel> = _loadedTransaction


    private fun addTransaction(transaction: Transaction) {
        viewModelScope.launch(Dispatchers.IO) {
            insertTransactionUseCase.invoke(transaction)
        }
    }

    private fun updateTransaction(transaction: Transaction) {
        viewModelScope.launch(Dispatchers.IO) {
            updateTransactionUseCase.invoke(transaction)
        }
    }

    fun selectDate(date: Date) {
        _selectedDate.postValue(date)
    }

    fun setUpData(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val transaction = transactionUiMapper.toTransactionUiModel(
                getTransactionByIdUseCase.invoke(id)
            )
            _loadedTransaction.postValue(transaction)
            _selectedAccountType.postValue(transaction.accountType)
            _selectedTrxCategory.postValue(transaction.category)
            _selectedDate.postValue(transaction.date)
        }
    }

    fun onBottomSheetDoneBtnClicked(
        id: Int,
        rawTransactionAmount: String,
        rawDescription: String,
        editingState: EditingState
    ): Boolean {
        val transactionAmount =
            transactionComponentsFormatter.formatTransactionAmount(rawTransactionAmount)
        val description = transactionComponentsFormatter.formatDescription(rawDescription)
        val trxCategory = selectedTrxCategory.value?.let {
            trxCategoryUiMapper.toTrxCategory(it)
        }
        val accountType = selectedAccountType.value
        val date = selectedDate.value ?: Calendar.getInstance().time

        if (trxCategory != null && accountType != null &&
            transactionAmount.isNotEmpty() && transactionAmount.toFloat() != 0f
        ) {
            val transaction = Transaction(
                id,
                trxCategory,
                date,
                description,
                accountType,
                transactionAmount.toFloat()
            )
            if (editingState == EditingState.NEW_TRANSACTION) {
                addTransaction(transaction)
            } else {
                updateTransaction(transaction)
            }
            return true
        }
        return false
    }

    fun selectAccountType(accountId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _selectedAccountType.postValue(
                getAccountTypeUseCase.invoke(accountId)
            )
        }
    }

    fun selectTrxCategory(trxCategoryId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _selectedTrxCategory.postValue(
                trxCategoryUiMapper.toTrxCategoryUiModel(
                    getTrxCategoryUseCase.invoke(trxCategoryId)
                )
            )
        }
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(
        private val getAccountTypeUseCase: GetAccountTypeUseCase,
        private val getTrxCategoryUseCase: GetTrxCategoryUseCase,

        private val insertTransactionUseCase: InsertTransactionUseCase,
        private val getTransactionByIdUseCase: GetTransactionByIdUseCase,
        private val updateTransactionUseCase: UpdateTransactionUseCase,

        private val transactionUiMapper: TransactionUiMapper,
        private val trxCategoryUiMapper: TrxCategoryUiMapper,
        private val transactionComponentsFormatter: TransactionComponentsFormatter
    ) : ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            AddTransactionViewModel(
                getAccountTypeUseCase,
                getTrxCategoryUseCase,
                insertTransactionUseCase,
                getTransactionByIdUseCase,
                updateTransactionUseCase,
                transactionUiMapper,
                trxCategoryUiMapper,
                transactionComponentsFormatter
            ) as T
    }
}