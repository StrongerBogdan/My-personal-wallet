package com.bogdanmurzin.mypersonalwallet.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bogdanmurzin.domain.entities.AccountType
import com.bogdanmurzin.domain.entities.CategoryEntity
import com.bogdanmurzin.mypersonalwallet.R
import com.bogdanmurzin.mypersonalwallet.adapter.ImageRecyclerViewAdapter
import com.bogdanmurzin.mypersonalwallet.common.Constants
import com.bogdanmurzin.mypersonalwallet.databinding.DialogCategoryChooseBinding
import com.bogdanmurzin.mypersonalwallet.ui.viewmodel.CategoryChooseViewModel
import com.bogdanmurzin.mypersonalwallet.util.CategoryArg
import com.bogdanmurzin.mypersonalwallet.util.setNavigationResult

class AccountTypeChooseDialogFragment : CategoryChooseDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        binding.titleAccountChoose.text = resources.getString(R.string.choose_account)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        observeViewModel()
        setupRecycler()
        // Get All user accounts and show them
        chooseViewModel.showAllAccounts()
    }

    override fun observeViewModel() {
        super.observeViewModel()
        // show all user account types
        chooseViewModel.accountTypes.observe(viewLifecycleOwner) {
            imageRecyclerAdapter.submitList(it)
        }
    }

    override fun setupRecycler() {
        imageRecyclerAdapter =
            ImageRecyclerViewAdapter { chooseViewModel.selectAccountType(it as AccountType) }
        super.setupRecycler()
    }

}