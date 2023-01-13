package com.bogdanmurzin.mypersonalwallet.ui.viewmodel

import androidx.lifecycle.*
import com.bogdanmurzin.domain.entities.AccountType
import com.bogdanmurzin.domain.usecases.account_type.DeleteAccountUseCase
import com.bogdanmurzin.domain.usecases.account_type.GetAccountTypeUseCase
import com.bogdanmurzin.domain.usecases.account_type.InsertAccountUseCase
import com.bogdanmurzin.domain.usecases.account_type.UpdateAccountUseCase
import com.bogdanmurzin.mypersonalwallet.util.CoroutineDispatcherProvider
import com.bogdanmurzin.mypersonalwallet.util.DefaultCoroutineDispatcherProvider
import com.bogdanmurzin.mypersonalwallet.util.EditingState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject


@HiltViewModel
class AddAccountViewModel @Inject constructor(
    private val getAccountTypeUseCase: GetAccountTypeUseCase,
    private val updateAccountUseCase: UpdateAccountUseCase,
    private val insertAccountUseCase: InsertAccountUseCase,
    private val deleteAccountUseCase: DeleteAccountUseCase,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider
) : ViewModel() {

    private val _loadedAccountType: MutableLiveData<AccountType> = MutableLiveData()
    val loadedAccountType: LiveData<AccountType> = _loadedAccountType
    private val _currentImageUrl: MutableLiveData<String?> = MutableLiveData(null)
    val currentImageUrl: LiveData<String?> = _currentImageUrl
    private val _doneAction: SingleLiveEvent<Result<Boolean>> = SingleLiveEvent()
    var doneAction: LiveData<Result<Boolean>> = _doneAction

    fun setUpData(id: Int) {
        viewModelScope.launch(coroutineDispatcherProvider.io()) {
            val account = getAccountTypeUseCase.invoke(id)
            // Save image URL
            _currentImageUrl.postValue(account.imageUri)
            _loadedAccountType.postValue(account)
        }
    }

    private fun validateData(id: Int, title: String, state: EditingState): Boolean {
        val image = currentImageUrl.value

        if (image != null && title.isNotEmpty()) {
            val accountType = AccountType(
                id,
                title,
                image
            )
            if (state == EditingState.NEW_TRANSACTION) {
                addAccount(accountType)
            } else {
                updateAccount(accountType)
            }
            return true
        }
        return false
    }

    fun addNewAccountType(id: Int, title: String, state: EditingState) {
        if (validateData(id, title, state)) {
            _doneAction.postValue(Result.success(true))
        } else {
            _doneAction.postValue(Result.failure(IOException("Not all fields have filled")))
        }
    }

    fun deleteAccountType(id: Int) {
        viewModelScope.launch(coroutineDispatcherProvider.io()) {
            deleteAccountUseCase.invoke(id)
        }
    }

    private fun updateAccount(accountType: AccountType) {
        viewModelScope.launch(coroutineDispatcherProvider.io()) {
            updateAccountUseCase.invoke(accountType)
        }
    }

    private fun addAccount(accountType: AccountType) {
        viewModelScope.launch(coroutineDispatcherProvider.io()) {
            insertAccountUseCase.invoke(accountType)
        }
    }

    fun setImageUrl(url: String) {
        _currentImageUrl.postValue(url)
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(
        private val getAccountTypeUseCase: GetAccountTypeUseCase,
        private val updateAccountUseCase: UpdateAccountUseCase,
        private val insertAccountUseCase: InsertAccountUseCase,
        private val deleteAccountUseCase: DeleteAccountUseCase,
        private val coroutineDispatcherProvider: DefaultCoroutineDispatcherProvider
    ) : ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            AddAccountViewModel(
                getAccountTypeUseCase,
                updateAccountUseCase,
                insertAccountUseCase,
                deleteAccountUseCase,
                coroutineDispatcherProvider
            ) as T
    }
}