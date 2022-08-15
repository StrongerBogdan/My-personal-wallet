package com.bogdanmurzin.mypersonalwallet.ui.viewmodel

import androidx.lifecycle.*
import com.bogdanmurzin.domain.entities.AccountType
import com.bogdanmurzin.domain.entities.TransactionCategory
import com.bogdanmurzin.domain.usecases.account_type.GetAccountTypeUseCase
import com.bogdanmurzin.domain.usecases.account_type.GetAllAccountTypesUseCase
import com.bogdanmurzin.domain.usecases.transaction_category.GetAllTrxCategoryUseCase
import com.bogdanmurzin.domain.usecases.transaction_category.GetAllTrxSubCategoriesUseCase
import com.bogdanmurzin.domain.usecases.transaction_category.GetTrxCategoryBySubcategoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryChooseViewModel @Inject constructor(
    private val getAccountTypeUseCase: GetAccountTypeUseCase,
    private val getAllAccountTypesUseCase: GetAllAccountTypesUseCase,

    private val getAllTrxCategoryUseCase: GetAllTrxCategoryUseCase,
    private val getAllTrxSubCategoriesUseCase: GetAllTrxSubCategoriesUseCase,
    private val getTrxCategoryBySubcategoryUseCase: GetTrxCategoryBySubcategoryUseCase
) : ViewModel() {

    private val _selectedAccountType: MutableLiveData<AccountType?> = MutableLiveData()
    val selectedAccountType: LiveData<AccountType?> = _selectedAccountType
    private val _selectedTrxCategory: MutableLiveData<TransactionCategory> = MutableLiveData()
    val selectedTrxCategory: LiveData<TransactionCategory> = _selectedTrxCategory
    private val _accountTypes: MutableLiveData<List<AccountType>> = MutableLiveData()
    var accountTypes: LiveData<List<AccountType>> = _accountTypes
    private val _trxSubCategories: MutableLiveData<List<TransactionCategory>> = MutableLiveData()
    var trxSubCategories: LiveData<List<TransactionCategory>> = _trxSubCategories
    private val _trxCategories: MutableLiveData<List<TransactionCategory>> = MutableLiveData()
    var trxCategories: LiveData<List<TransactionCategory>> = _trxCategories

    private var selectedCategoryTitle: String? = null
    private var selectedSubcategoryTitle: String? = null

    fun chooseCategory() {
        val selectedCategoryTitle = selectedCategoryTitle

        if (selectedCategoryTitle != null) {
            selectTransactionCategory(selectedCategoryTitle)
        }
    }

    private fun selectTransactionCategory(selectedCategoryTitle: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val trxCategory = getTrxCategoryIdBySubcategory(
                selectedCategoryTitle,
                selectedSubcategoryTitle
            )
            // Save category
            _selectedTrxCategory.postValue(
                trxCategory
            )
        }
    }

    fun selectAccountType(accountType: AccountType) {
        viewModelScope.launch(Dispatchers.IO) {
            _selectedAccountType.postValue(
                getAccountTypeUseCase.invoke(accountType.id)
            )
        }
    }

    private suspend fun getTrxCategoryIdBySubcategory(title: String, subcategory: String?)
            : TransactionCategory = getTrxCategoryBySubcategoryUseCase.invoke(title, subcategory)

    fun showAllAccounts() {
        viewModelScope.launch(Dispatchers.IO) {
            getAllAccountTypesUseCase.invoke().collect {
                _accountTypes.postValue(it)
            }
        }
    }

    fun showAllTrxCategories() {
        viewModelScope.launch(Dispatchers.IO) {
            getAllTrxCategoryUseCase.invoke().collect {
                _trxCategories.postValue(it)
            }
        }
    }

    fun selectSubcategory(chipSubcategory: String?) {
        selectedSubcategoryTitle = chipSubcategory
    }

    fun loadAllTrxSubCategories(trxCategory: TransactionCategory) {
        viewModelScope.launch(Dispatchers.IO) {
            // Save title
            selectedCategoryTitle = trxCategory.title
            // Load all subcategories selected category (by title)
            getAllTrxSubCategoriesUseCase.invoke(trxCategory.title).collect {
                _trxSubCategories.postValue(it)
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(
        private val getAccountTypeUseCase: GetAccountTypeUseCase,
        private val getAllAccountTypesUseCase: GetAllAccountTypesUseCase,

        private val getAllTrxCategoryUseCase: GetAllTrxCategoryUseCase,
        private val getAllTrxSubCategoriesUseCase: GetAllTrxSubCategoriesUseCase,
        private val getTrxCategoryBySubcategoryUseCase: GetTrxCategoryBySubcategoryUseCase
    ) : ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            CategoryChooseViewModel(
                getAccountTypeUseCase,
                getAllAccountTypesUseCase,
                getAllTrxCategoryUseCase,
                getAllTrxSubCategoriesUseCase,
                getTrxCategoryBySubcategoryUseCase
            ) as T
    }
}