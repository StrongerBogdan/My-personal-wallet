package com.bogdanmurzin.mypersonalwallet.ui.viewmodel

import androidx.lifecycle.*
import com.bogdanmurzin.domain.entities.AccountType
import com.bogdanmurzin.domain.usecases.account_type.GetAccountTypeUseCase
import com.bogdanmurzin.domain.usecases.account_type.InsertAccountUseCase
import com.bogdanmurzin.domain.usecases.account_type.UpdateAccountUseCase
import com.bogdanmurzin.mypersonalwallet.util.EditingState
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.util.ByteArrayBuffer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.BufferedInputStream
import java.net.URL
import javax.inject.Inject

@HiltViewModel
class AddAccountViewModel @Inject constructor(
    private val getAccountTypeUseCase: GetAccountTypeUseCase,
    private val updateAccountUseCase: UpdateAccountUseCase,
    private val insertAccountUseCase: InsertAccountUseCase
) : ViewModel() {

    private val _loadedAccountType: MutableLiveData<AccountType> = MutableLiveData()
    val loadedAccountType: LiveData<AccountType> = _loadedAccountType
    private val _currentImageUrl: MutableLiveData<String?> = MutableLiveData(null)
    val currentImageUrl: LiveData<String?> = _currentImageUrl

    //var currentImageUrl: String? = null

    fun setUpData(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val account = getAccountTypeUseCase.invoke(id)
            // Save image URL
            _currentImageUrl.postValue(account.imageUri)
            _loadedAccountType.postValue(account)
        }
    }

    fun onBottomSheetDoneBtnClicked(
        id: Int,
        title: String,
        state: EditingState
    ): Boolean {
        // TODO GET and save image
        val image = getImage(currentImageUrl.value)

        if (image != null && title.isNotEmpty()) {
            val accountType = AccountType(
                id,
                title,
                // TODO DELETE TEST URI
                imageUri = currentImageUrl.value
                    ?: "https://cdn0.iconfinder.com/data/icons/education-364/24/test-flask-science-beaker-school-education-learning-256.png"
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

    private fun updateAccount(accountType: AccountType) {
        viewModelScope.launch(Dispatchers.IO) {
            updateAccountUseCase.invoke(accountType)
        }
    }

    private fun addAccount(accountType: AccountType) {
        viewModelScope.launch(Dispatchers.IO) {
            insertAccountUseCase.invoke(accountType)
        }
    }

    fun setImageUrl(url: String) {
        _currentImageUrl.postValue(url)
    }

    private fun getImage(url: String?): ByteArray? {
        if (url == null) return null

        val imageUrl = URL(url)
        val urlConnection = imageUrl.openConnection()
        val inputStream = urlConnection.getInputStream()
        val bufferedInputStream = BufferedInputStream(inputStream)
        val byteArrayBuffer = ByteArrayBuffer(500)
        var current = 0
        while (bufferedInputStream.read().also { current = it } != -1) {
            byteArrayBuffer.append(current)
        }
        return byteArrayBuffer.toByteArray()
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(
        private val getAccountTypeUseCase: GetAccountTypeUseCase,
        private val updateAccountUseCase: UpdateAccountUseCase,
        private val insertAccountUseCase: InsertAccountUseCase
    ) : ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            AddAccountViewModel(
                getAccountTypeUseCase,
                updateAccountUseCase,
                insertAccountUseCase
            ) as T
    }
}