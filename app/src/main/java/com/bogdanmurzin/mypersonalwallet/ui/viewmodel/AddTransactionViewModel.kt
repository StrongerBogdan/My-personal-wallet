package com.bogdanmurzin.mypersonalwallet.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bogdanmurzin.domain.entities.AccountType
import com.bogdanmurzin.domain.entities.Transaction
import com.bogdanmurzin.domain.entities.TransactionCategory
import com.bogdanmurzin.domain.usecases.GetAccountIdUseCase
import com.bogdanmurzin.domain.usecases.GetAccountTypeUseCase
import com.bogdanmurzin.domain.usecases.GetAllAccountTypesUseCase
import com.bogdanmurzin.domain.usecases.transaction_category.GetAllTrxCategoryUseCase
import com.bogdanmurzin.domain.usecases.transaction_category.GetTrxCategoryIdUseCase
import com.bogdanmurzin.domain.usecases.transaction_category.GetTrxCategoryUseCase
import com.bogdanmurzin.mypersonalwallet.common.Constants.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class AddTransactionViewModel @Inject constructor(
    private val getAccountTypeUseCase: GetAccountTypeUseCase,
    private val getAllAccountTypesUseCase: GetAllAccountTypesUseCase,
    private val getAccountIdUseCase: GetAccountIdUseCase,

    private val getTrxCategoryUseCase: GetTrxCategoryUseCase,
    private val getAllTrxCategoryUseCase: GetAllTrxCategoryUseCase,
    private val getTrxCategoryIdUseCase: GetTrxCategoryIdUseCase
) : ViewModel() {

    private val _transactionLiveData: MutableLiveData<Transaction>? = null
    val transactionLiveData: LiveData<Transaction>? = _transactionLiveData
    private val _selectedAccountType: MutableLiveData<AccountType> = MutableLiveData()
    val selectedAccountType: LiveData<AccountType> = _selectedAccountType
    private val _selectedTrxCategory: MutableLiveData<TransactionCategory> = MutableLiveData()
    val selectedTrxCategory: LiveData<TransactionCategory> = _selectedTrxCategory

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

    suspend fun getAllTrxCategories(): Flow<List<TransactionCategory>> =
        getAllTrxCategoryUseCase.invoke()

    suspend fun getTrxCategoryId(trxCategory: TransactionCategory): Int =
        getTrxCategoryIdUseCase.invoke(trxCategory)

    @Suppress("UNCHECKED_CAST")
    class Factory(
        private val getAccountTypeUseCase: GetAccountTypeUseCase,
        private val getAllAccountTypesUseCase: GetAllAccountTypesUseCase,
        private val getAccountIdUseCase: GetAccountIdUseCase,

        private val getTrxCategoryUseCase: GetTrxCategoryUseCase,
        private val getAllTrxCategoryUseCase: GetAllTrxCategoryUseCase,
        private val getTrxCategoryIdUseCase: GetTrxCategoryIdUseCase
    ) : ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            AddTransactionViewModel(
                getAccountTypeUseCase,
                getAllAccountTypesUseCase,
                getAccountIdUseCase,
                getTrxCategoryUseCase,
                getAllTrxCategoryUseCase,
                getTrxCategoryIdUseCase
            ) as T
    }
}