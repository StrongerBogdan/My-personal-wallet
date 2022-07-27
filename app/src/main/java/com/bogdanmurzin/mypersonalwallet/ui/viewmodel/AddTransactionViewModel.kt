package com.bogdanmurzin.mypersonalwallet.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bogdanmurzin.domain.entities.AccountType
import com.bogdanmurzin.domain.entities.Transaction
import com.bogdanmurzin.domain.usecases.GetAccountIdUseCase
import com.bogdanmurzin.domain.usecases.GetAccountTypeUseCase
import com.bogdanmurzin.domain.usecases.GetAllAccountTypesUseCase
import com.bogdanmurzin.mypersonalwallet.common.Constants.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class AddTransactionViewModel @Inject constructor(
    private val getAccountTypeUseCase: GetAccountTypeUseCase,
    private val getAllAccountTypesUseCase: GetAllAccountTypesUseCase,
    private val getAccountIdUseCase: GetAccountIdUseCase
) : ViewModel() {

    private val _transactionLiveData: MutableLiveData<Transaction>? = null
    val transactionLiveData: LiveData<Transaction>? = _transactionLiveData
    private val _selectedAccountType: MutableLiveData<AccountType> = MutableLiveData()
    val selectedAccountType: LiveData<AccountType> = _selectedAccountType

    suspend fun getAccount(id: Int) {
        val selectedAccountType =
            getAccountTypeUseCase.invoke(id)
        Log.i(TAG, "getAccount: ${selectedAccountType.title} id = $id")
        _selectedAccountType.postValue(selectedAccountType)
    }

    suspend fun getAllAccounts(): Flow<List<AccountType>> =
        getAllAccountTypesUseCase.invoke()

    suspend fun getAccountId(account: AccountType): Int =
        getAccountIdUseCase.invoke(account)

    @Suppress("UNCHECKED_CAST")
    class Factory(
        private val getAccountTypeUseCase: GetAccountTypeUseCase,
        private val getAllAccountTypesUseCase: GetAllAccountTypesUseCase,
        private val getAccountIdUseCase: GetAccountIdUseCase
    ) : ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            AddTransactionViewModel(
                getAccountTypeUseCase,
                getAllAccountTypesUseCase,
                getAccountIdUseCase
            ) as T
    }
}