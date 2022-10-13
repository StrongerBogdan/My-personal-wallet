package com.bogdanmurzin.mypersonalwallet.ui.viewmodel

import androidx.lifecycle.*
import androidx.navigation.NavDirections
import com.bogdanmurzin.domain.entities.TransactionCategory
import com.bogdanmurzin.domain.usecases.transaction_category.GetAllTrxCategoryUseCase
import com.bogdanmurzin.mypersonalwallet.ui.fragment.TrxCategoryFragmentDirections
import com.bogdanmurzin.mypersonalwallet.util.CoroutineDispatcherProvider
import com.bogdanmurzin.mypersonalwallet.util.DefaultCoroutineDispatcherProvider
import com.bogdanmurzin.mypersonalwallet.util.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrxCategoryViewModel @Inject constructor(
    private val getAllTrxCategoryUseCase: GetAllTrxCategoryUseCase,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider
) : ViewModel() {
    private val _trxCategories: MutableLiveData<List<TransactionCategory>> = MutableLiveData()
    var trxCategories: LiveData<List<TransactionCategory>> = _trxCategories
    private val _action: SingleLiveEvent<NavDirections> = SingleLiveEvent()
    val action: SingleLiveEvent<NavDirections> = _action

    init {
        showAllTrxCategories()
    }

    fun showAllTrxCategories() {
        viewModelScope.launch(coroutineDispatcherProvider.io()) {
            getAllTrxCategoryUseCase.invoke().collect {
                _trxCategories.postValue(it)
            }
        }
    }

    fun openBottomSheet(event: Event) {
        if (event is Event.OpenPreviewScreen) {
            action.postValue(
                TrxCategoryFragmentDirections
                    .actionCategoryFragmentToAddTrxCategoryFlowGraph(event.id)
            )
        }
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(
        private val getAllTrxCategoryUseCase: GetAllTrxCategoryUseCase,
        private val coroutineDispatcherProvider: DefaultCoroutineDispatcherProvider
    ) : ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            TrxCategoryViewModel(
                getAllTrxCategoryUseCase,
                coroutineDispatcherProvider
            ) as T
    }
}