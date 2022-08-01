package com.bogdanmurzin.mypersonalwallet.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
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

    private val transactionUiMapper: TransactionUiMapper
) : ViewModel() {

    private val _selectedAccountType: MutableLiveData<AccountType> = MutableLiveData()
    val selectedAccountType: LiveData<AccountType> = _selectedAccountType
    private val _selectedTrxCategory: MutableLiveData<TransactionCategory> = MutableLiveData()
    val selectedTrxCategory: LiveData<TransactionCategory> = _selectedTrxCategory
    private val _selectedDate: MutableLiveData<Date> = MutableLiveData(Calendar.getInstance().time)
    val selectedDate: LiveData<Date> = _selectedDate

    // external read-only variable
    var selectedCategoryTitle: String? = null
        private set
    var selectedSubcategoryTitle: String? = null

    suspend fun getAccount(id: Int) {
        val selectedAccountType =
            getAccountTypeUseCase.invoke(id)
        _selectedAccountType.postValue(selectedAccountType)
    }

    suspend fun getAllAccounts(): Flow<List<AccountType>> =
        getAllAccountTypesUseCase.invoke()

    suspend fun getAccountId(account: AccountType): Int =
        getAccountIdUseCase.invoke(account)

    suspend fun getTrxCategory(id: Int) {
        val selectedTrxCategory =
            getTrxCategoryUseCase.invoke(id)
        _selectedTrxCategory.postValue(selectedTrxCategory)
    }

    suspend fun getAllTrxSubCategories(id: Int): Flow<List<TransactionCategory>> {
        val selectedTrxCategory =
            getTrxCategoryUseCase.invoke(id)
        // Save title
        selectedCategoryTitle = selectedTrxCategory.title
        return getAllTrxSubCategoriesUseCase.invoke(selectedTrxCategory.title)
    }

    suspend fun getAllTrxCategories(): Flow<List<TransactionCategory>> =
        getAllTrxCategoryUseCase.invoke()

    suspend fun getTrxCategoryId(trxCategory: TransactionCategory): Int =
        getTrxCategoryIdUseCase.invoke(trxCategory)

    suspend fun getTrxCategoryIdBySubcategory(title: String, subcategory: String?): Int =
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
        _selectedTrxCategory.postValue(transaction.category)
        _selectedDate.postValue(transaction.date)
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

        private val transactionUiMapper: TransactionUiMapper
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
                transactionUiMapper
            ) as T
    }
}