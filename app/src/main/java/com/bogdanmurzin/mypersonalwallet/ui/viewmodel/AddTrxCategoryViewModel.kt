package com.bogdanmurzin.mypersonalwallet.ui.viewmodel

import androidx.lifecycle.*
import com.bogdanmurzin.domain.entities.TransactionCategory
import com.bogdanmurzin.domain.usecases.transaction_category.*
import com.bogdanmurzin.mypersonalwallet.util.EditingState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddTrxCategoryViewModel @Inject constructor(
    private val getTrxCategoryUseCase: GetTrxCategoryUseCase,
    private val updateTrxCategoryUseCase: UpdateTrxCategoryUseCase,
    private val insertTrxCategoryUseCase: InsertTrxCategoryUseCase,
    private val getAllTrxSubCategoriesUseCase: GetAllTrxSubCategoriesUseCase
) : ViewModel() {

    private val _selectedTrxCategory: MutableLiveData<TransactionCategory> = MutableLiveData()
    val loadedTrxCategories: LiveData<TransactionCategory> = _selectedTrxCategory
    private val _currentImageUrl: MutableLiveData<String?> = MutableLiveData(null)
    val currentImageUrl: LiveData<String?> = _currentImageUrl
    private val _trxSubCategories: MutableLiveData<List<TransactionCategory>> = MutableLiveData()
    var trxSubCategories: LiveData<List<TransactionCategory>> = _trxSubCategories

    private var selectedTrxCategoryTitle: String? = null
    private var selectedTrxSubcategoryTitle: String? = null

    fun setUpData(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
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

    fun addNewTrxCategory(
        id: Int,
        category: String,
        state: EditingState
    ): Boolean {
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

    private fun updateTrxCategory(trxCategory: TransactionCategory) {
        viewModelScope.launch(Dispatchers.IO) {
            updateTrxCategoryUseCase.invoke(trxCategory)
        }
    }

    private fun addTrxCategory(trxCategory: TransactionCategory) {
        viewModelScope.launch(Dispatchers.IO) {
            insertTrxCategoryUseCase.invoke(trxCategory)
        }
    }

    fun setImageUrl(url: String) {
        _currentImageUrl.postValue(url)
    }

    fun selectSubcategory(chipSubcategory: String?) {
        selectedTrxSubcategoryTitle = chipSubcategory
    }

    fun loadAllTrxSubCategories(trxCategory: TransactionCategory) {
        viewModelScope.launch(Dispatchers.IO) {
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
        private val getAllTrxSubCategoriesUseCase: GetAllTrxSubCategoriesUseCase
    ) : ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            AddTrxCategoryViewModel(
                getTrxCategoryUseCase,
                updateTrxCategoryUseCase,
                insertTrxCategoryUseCase,
                getAllTrxSubCategoriesUseCase
            ) as T
    }
}