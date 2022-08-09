package com.bogdanmurzin.mypersonalwallet.ui.viewmodel

import androidx.lifecycle.*
import androidx.navigation.NavDirections
import com.bogdanmurzin.domain.entities.AccountType
import com.bogdanmurzin.domain.usecases.account_type.GetAllAccountTypesUseCase
import com.bogdanmurzin.mypersonalwallet.ui.fragment.AccountFragmentDirections
import com.bogdanmurzin.mypersonalwallet.ui.fragment.FragmentMoneyTransactionsDirections
import com.bogdanmurzin.mypersonalwallet.util.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val getAllAccountTypesUseCase: GetAllAccountTypesUseCase
) : ViewModel() {

    private val _accountTypes: MutableLiveData<List<AccountType>> = MutableLiveData()
    var accountTypes: LiveData<List<AccountType>> = _accountTypes
    private val _action: SingleLiveEvent<NavDirections> = SingleLiveEvent()
    val action: SingleLiveEvent<NavDirections> = _action

    init {
        showAllAccounts()
    }

    fun showAllAccounts() {
        viewModelScope.launch(Dispatchers.IO) {
            getAllAccountTypesUseCase.invoke().collect {
                _accountTypes.postValue(it)
            }
        }
    }

    fun openBottomSheet(event: Event) {
        if (event is Event.OpenPreviewScreen) {
            action.postValue(
                AccountFragmentDirections
                    .actionAccountFragmentToAddAccountFlowGraph(event.id)
            )
        }
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(
        private val getAllAccountTypesUseCase: GetAllAccountTypesUseCase,
    ) : ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            AccountViewModel(
                getAllAccountTypesUseCase
            ) as T
    }
}