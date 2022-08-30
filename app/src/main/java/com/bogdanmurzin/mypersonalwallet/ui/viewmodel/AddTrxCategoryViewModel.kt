package com.bogdanmurzin.mypersonalwallet.ui.viewmodel

import androidx.lifecycle.*
import com.bogdanmurzin.domain.entities.TransactionCategory
import com.bogdanmurzin.domain.usecases.transaction_category.GetAllTrxSubCategoriesUseCase
import com.bogdanmurzin.domain.usecases.transaction_category.GetTrxCategoryUseCase
import com.bogdanmurzin.domain.usecases.transaction_category.InsertTrxCategoryUseCase
import com.bogdanmurzin.domain.usecases.transaction_category.UpdateTrxCategoryUseCase
import com.bogdanmurzin.mypersonalwallet.util.CoroutineDispatcherProvider
import com.bogdanmurzin.mypersonalwallet.util.EditingState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class AddTrxCategoryViewModel @Inject constructor(
    private val getTrxCategoryUseCase: GetTrxCategoryUseCase,
    private val updateTrxCategoryUseCase: UpdateTrxCategoryUseCase,
    private val insertTrxCategoryUseCase: InsertTrxCategoryUseCase,
    private val getAllTrxSubCategoriesUseCase: GetAllTrxSubCategoriesUseCase,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider
) : ViewModel() {

    private val _selectedTrxCategory: MutableLiveData<TransactionCategory> = MutableLiveData()
    val loadedTrxCategories: LiveData<TransactionCategory> = _selectedTrxCategory
    private val _currentImageUrl: MutableLiveData<String?> = MutableLiveData(null)
    val currentImageUrl: LiveData<String?> = _currentImageUrl
    private val _trxSubCategories: MutableLiveData<List<TransactionCategory>> = MutableLiveData()
    var trxSubCategories: LiveData<List<TransactionCategory>> = _trxSubCategories
    private val _doneAction: SingleLiveEvent<Result<Boolean>> = SingleLiveEvent()
    var doneAction: LiveData<Result<Boolean>> = _doneAction

    private var selectedTrxCategoryTitle: String? = null
    private var selectedTrxSubcategoryTitle: String? = null

    fun setUpData(id: Int) {
        viewModelScope.launch(coroutineDispatcherProvider.io()) {
            val trxCategory = getTrxCategoryUseCase.invoke(id)
            // Save image URL
            _currentImageUrl.postValue(trxCategory.imageUri)
            _selectedTrxCategory.postValue(trxCategory)
            // Save title
            selectedTrxCategoryTitle = trxCategory.title
            // Load all subcategories
            loadAllTrxSubCategories(trxCategory)
        }
    }

    private fun validateData(id: Int, category: String, state: EditingState): Boolean {

        val image = currentImageUrl.value

        if (image != null && category.isNotEmpty()) {
            val trxCategory = TransactionCategory(
                id,
                category,
                selectedTrxSubcategoryTitle,
                image
            )
            if (state == EditingState.NEW_TRANSACTION) {
                addTrxCategory(trxCategory)
            } else {
                updateTrxCategory(trxCategory)
            }
            return true
        }
        return false
    }

    fun addNewTrxCategory(id: Int, category: String, state: EditingState) {
        if (validateData(id, category, state)) {
            _doneAction.postValue(Result.success(true))
        } else {
            _doneAction.postValue(Result.failure(IOException("Not all fields have filled")))
        }
    }

    private fun updateTrxCategory(trxCategory: TransactionCategory) {
        viewModelScope.launch(coroutineDispatcherProvider.io()) {
            updateTrxCategoryUseCase.invoke(trxCategory)
        }
    }

    private fun addTrxCategory(trxCategory: TransactionCategory) {
        viewModelScope.launch(coroutineDispatcherProvider.io()) {
            insertTrxCategoryUseCase.invoke(trxCategory)
        }
    }

    fun setImageUrl(url: String) {
        _currentImageUrl.postValue(url)
    }

    fun selectSubcategory(chipSubcategory: String?) {
        selectedTrxSubcategoryTitle = chipSubcategory
    }

    private fun loadAllTrxSubCategories(trxCategory: TransactionCategory) {
        viewModelScope.launch(coroutineDispatcherProvider.io()) {
            // Load all subcategories selected category (by title)
            getAllTrxSubCategoriesUseCase.invoke(trxCategory.title).collect {
                _trxSubCategories.postValue(it)
            }
        }
    }

    fun addNewSubcategory(subcategoryTitle: String): Boolean {
        // Get all selected data like category title and imageUrl
        val categoryTitle = selectedTrxCategoryTitle
        val imageUrl = currentImageUrl.value
        if (subcategoryTitle.isNotEmpty() && !categoryTitle.isNullOrEmpty() && !imageUrl.isNullOrEmpty()) {
            val trxCategory = TransactionCategory(0, categoryTitle, subcategoryTitle, imageUrl)
            addTrxCategory(trxCategory)
            return true
        }
        return false
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(
        private val getTrxCategoryUseCase: GetTrxCategoryUseCase,
        private val updateTrxCategoryUseCase: UpdateTrxCategoryUseCase,
        private val insertTrxCategoryUseCase: InsertTrxCategoryUseCase,
        private val getAllTrxSubCategoriesUseCase: GetAllTrxSubCategoriesUseCase,
        private val coroutineDispatcherProvider: CoroutineDispatcherProvider
    ) : ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            AddTrxCategoryViewModel(
                getTrxCategoryUseCase,
                updateTrxCategoryUseCase,
                insertTrxCategoryUseCase,
                getAllTrxSubCategoriesUseCase,
                coroutineDispatcherProvider
            ) as T
    }
}