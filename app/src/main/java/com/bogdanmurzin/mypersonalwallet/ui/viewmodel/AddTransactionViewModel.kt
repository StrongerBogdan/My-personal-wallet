package com.bogdanmurzin.mypersonalwallet.ui.viewmodel

import androidx.lifecycle.*
import com.bogdanmurzin.data.entity.AccountTypeEntity
import com.bogdanmurzin.domain.entities.AccountType
import com.bogdanmurzin.domain.entities.Transaction
import com.bogdanmurzin.domain.usecases.GetAccountTypesUseCase
import com.bogdanmurzin.domain.usecases.GetTransactionsUseCase
import com.bogdanmurzin.mypersonalwallet.mapper.TransactionUiMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddTransactionViewModel @Inject constructor(
    private val getAccountTypesUseCase: GetAccountTypesUseCase
) : ViewModel() {

    private val _transactionLiveData: MutableLiveData<Transaction>? = null
    val transactionLiveData: LiveData<Transaction>? = _transactionLiveData
    private val _accountType: MutableLiveData<AccountType> = MutableLiveData()
    val accountType: LiveData<AccountType> = _accountType

    suspend fun getAccount(id: Int) {
        val selectedAccountType =
            //getAccountTypesUseCase.invoke(id)
            AccountType(
                "title1",
                "https://cdn4.iconfinder.com/data/icons/48-bubbles/48/07.Wallet-48.png"
            )
        _accountType.postValue(selectedAccountType)
    }


    @Suppress("UNCHECKED_CAST")
    class Factory(
        val getAccountTypesUseCase: GetAccountTypesUseCase
    ) : ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            AddTransactionViewModel(getAccountTypesUseCase) as T
    }
}