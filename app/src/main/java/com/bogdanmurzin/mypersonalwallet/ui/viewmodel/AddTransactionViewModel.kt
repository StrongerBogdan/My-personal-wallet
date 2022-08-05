package com.bogdanmurzin.mypersonalwallet.ui.viewmodel

import androidx.lifecycle.*
import com.bogdanmurzin.domain.entities.AccountType
import com.bogdanmurzin.domain.entities.Transaction
import com.bogdanmurzin.domain.entities.TransactionCategory
import com.bogdanmurzin.domain.usecases.account_type.GetAccountIdUseCase
import com.bogdanmurzin.domain.usecases.account_type.GetAccountTypeUseCase
import com.bogdanmurzin.domain.usecases.account_type.GetAllAccountTypesUseCase
import com.bogdanmurzin.domain.usecases.transaction.GetTransactionByIdUseCase
import com.bogdanmurzin.domain.usecases.transaction.InsertTransactionUseCase
import com.bogdanmurzin.domain.usecases.transaction.UpdateTransactionUseCase
import com.bogdanmurzin.domain.usecases.transaction_category.*
import com.bogdanmurzin.mypersonalwallet.data.transaction_recycer_items.TransactionItemUiModel
import com.bogdanmurzin.mypersonalwallet.mapper.TransactionUiMapper
import com.bogdanmurzin.mypersonalwallet.mapper.TrxCategoryUiMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AddTransactionViewModel @Inject constructor(
    private val getAccountTypeUseCase: GetAccountTypeUseCase,
    private val getAllAccountTypesUseCase: GetAllAccountTypesUseCase,
    private val getAccountIdUseCase: GetAccountIdUseCase,

    private val getTrxCategoryUseCase: GetTrxCategoryUseCase,
    private val getAllTrxCategoryUseCase: GetAllTrxCategoryUseCase,
    private val getTrxCategoryIdUseCase: GetTrxCategoryIdUseCase,
    private val getAllTrxSubCategoriesUseCase: GetAllTrxSubCategoriesUseCase,
    private val getTrxCategoryIdBySubcategoryUseCase: GetTrxCategoryIdBySubcategoryUseCase,

    private val insertTransactionUseCase: InsertTransactionUseCase,
    private val getTransactionByIdUseCase: GetTransactionByIdUseCase,
    private val updateTransactionUseCase: UpdateTransactionUseCase,

    private val transactionUiMapper: TransactionUiMapper,
    private val trxCategoryUiMapper: TrxCategoryUiMapper
) : ViewModel() {

    private val _selectedAccountType: MutableLiveData<AccountType> = MutableLiveData()
    val selectedAccountType: LiveData<AccountType> = _selectedAccountType
    private val _selectedTrxCategory: MutableLiveData<TransactionCategory> = MutableLiveData()
    val selectedTrxCategory: LiveData<TransactionCategory> = _selectedTrxCategory
    private val _selectedDate: MutableLiveData<Date> = MutableLiveData(Calendar.getInstance().time)
    val selectedDate: LiveData<Date> = _selectedDate
    private val _trxCategories: MutableLiveData<List<TransactionCategory>> = MutableLiveData()
    var trxCategories: LiveData<List<TransactionCategory>> = _trxCategories
    private val _accountTypes: MutableLiveData<List<AccountType>> = MutableLiveData()
    var accountTypes: LiveData<List<AccountType>> = _accountTypes
    private val _trxSubCategories: MutableLiveData<List<TransactionCategory>> = MutableLiveData()
    var trxSubCategories: LiveData<List<TransactionCategory>> = _trxSubCategories
    private val _selectedSubcategoryTitle: MutableLiveData<String?> = MutableLiveData(null)
    var selectedSubcategoryTitle: LiveData<String?> = _selectedSubcategoryTitle
    private val _selectedCategoryTitle: MutableLiveData<String?> = MutableLiveData(null)
    var selectedCategoryTitle: LiveData<String?> = _selectedCategoryTitle

    // external read-only variable
    //var selectedCategoryTitle: String? = null
    //private set
    //var selectedSubcategoryTitle: String? = null

    suspend fun getTrxCategory(id: Int) {
        val selectedTrxCategory = getTrxCategoryUseCase.invoke(id)
        _selectedTrxCategory.postValue(selectedTrxCategory)
    }

    fun loadAllTrxSubCategories(trxCategory: TransactionCategory) {
        viewModelScope.launch(Dispatchers.IO) {
            // Save title
            _selectedCategoryTitle.postValue(trxCategory.title)
            // Load all subcategories selected category (by title)
            getAllTrxSubCategoriesUseCase.invoke(trxCategory.title).collect {
                _trxSubCategories.postValue(it)
            }
        }
    }

    private suspend fun getTrxCategoryIdBySubcategory(title: String, subcategory: String?): Int =
        getTrxCategoryIdBySubcategoryUseCase.invoke(title, subcategory)

    suspend fun addTransaction(transaction: Transaction) {
        insertTransactionUseCase.invoke(transaction)
    }

    suspend fun updateTransaction(transaction: Transaction) {
        updateTransactionUseCase.invoke(transaction)
    }

    suspend fun getTransactionById(id: Int): TransactionItemUiModel =
        transactionUiMapper.toTransactionUiModel(
            getTransactionByIdUseCase.invoke(id)
        )

    fun selectDate(date: Date) {
        _selectedDate.postValue(date)

    }

    fun setUpData(transaction: TransactionItemUiModel) {
        _selectedAccountType.postValue(transaction.accountType)
        _selectedTrxCategory.postValue(trxCategoryUiMapper.toTrxCategory(transaction.category))
        _selectedDate.postValue(transaction.date)
    }

    private fun selectTransactionCategory(selectedCategoryTitle: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val trxId = getTrxCategoryIdBySubcategory(
                selectedCategoryTitle,
                selectedSubcategoryTitle.value
            )
            getTrxCategory(trxId)
        }
    }

    fun selectAccountType(accountType: AccountType) {
        viewModelScope.launch(Dispatchers.IO) {
            _selectedAccountType.postValue(
                getAccountTypeUseCase.invoke(accountType.id)
            )
        }
    }

    fun showAllTrxCategories() {
        viewModelScope.launch(Dispatchers.IO) {
            getAllTrxCategoryUseCase.invoke().collect {
                _trxCategories.postValue(it)
            }
        }
    }

    fun showAllAccounts() {
        viewModelScope.launch(Dispatchers.IO) {
            getAllAccountTypesUseCase.invoke().collect {
                _accountTypes.postValue(it)
            }
        }
    }

    fun selectSubcategory(chipSubcategory: String?) {
        _selectedSubcategoryTitle.postValue(chipSubcategory)
    }

    fun onDoneBtnClicked() {
        val selectedCategoryTitle = selectedCategoryTitle.value

        if (selectedCategoryTitle != null) {
            selectTransactionCategory(selectedCategoryTitle)
        }
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(
        private val getAccountTypeUseCase: GetAccountTypeUseCase,
        private val getAllAccountTypesUseCase: GetAllAccountTypesUseCase,
        private val getAccountIdUseCase: GetAccountIdUseCase,

        private val getTrxCategoryUseCase: GetTrxCategoryUseCase,
        private val getAllTrxCategoryUseCase: GetAllTrxCategoryUseCase,
        private val getTrxCategoryIdUseCase: GetTrxCategoryIdUseCase,
        private val getAllTrxSubCategoriesUseCase: GetAllTrxSubCategoriesUseCase,
        private val getTrxCategoryIdBySubcategoryUseCase: GetTrxCategoryIdBySubcategoryUseCase,

        private val insertTransactionUseCase: InsertTransactionUseCase,
        private val getTransactionByIdUseCase: GetTransactionByIdUseCase,
        private val updateTransactionUseCase: UpdateTransactionUseCase,

        private val transactionUiMapper: TransactionUiMapper,
        private val trxCategoryUiMapper: TrxCategoryUiMapper
    ) : ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            AddTransactionViewModel(
                getAccountTypeUseCase,
                getAllAccountTypesUseCase,
                getAccountIdUseCase,
                getTrxCategoryUseCase,
                getAllTrxCategoryUseCase,
                getTrxCategoryIdUseCase,
                getAllTrxSubCategoriesUseCase,
                getTrxCategoryIdBySubcategoryUseCase,
                insertTransactionUseCase,
                getTransactionByIdUseCase,
                updateTransactionUseCase,
                transactionUiMapper,
                trxCategoryUiMapper
            ) as T
    }
}