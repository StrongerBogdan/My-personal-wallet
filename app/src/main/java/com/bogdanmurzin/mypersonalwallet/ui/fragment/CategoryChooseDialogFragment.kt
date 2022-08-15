package com.bogdanmurzin.mypersonalwallet.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bogdanmurzin.domain.entities.AccountType
import com.bogdanmurzin.domain.entities.CategoryEntity
import com.bogdanmurzin.domain.entities.TransactionCategory
import com.bogdanmurzin.mypersonalwallet.R
import com.bogdanmurzin.mypersonalwallet.adapter.ImageRecyclerViewAdapter
import com.bogdanmurzin.mypersonalwallet.common.Constants
import com.bogdanmurzin.mypersonalwallet.common.Constants.SPAN_COUNT
import com.bogdanmurzin.mypersonalwallet.databinding.DialogCategoryChooseBinding
import com.bogdanmurzin.mypersonalwallet.ui.viewmodel.CategoryChooseViewModel
import com.bogdanmurzin.mypersonalwallet.util.CategoryArg
import com.bogdanmurzin.mypersonalwallet.util.setNavigationResult
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
abstract class CategoryChooseDialogFragment : DialogFragment() {

    protected lateinit var binding: DialogCategoryChooseBinding
    protected lateinit var imageRecyclerAdapter: ImageRecyclerViewAdapter
    protected val chooseViewModel: CategoryChooseViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogCategoryChooseBinding.inflate(layoutInflater)

        return binding.root
    }

    protected open fun observeViewModel() {
        chooseViewModel.selectedAccountType.observe(viewLifecycleOwner) {
            setNavigationResult(it?.id, Constants.ACCOUNT_TYPE_RESULT_KEY)
            findNavController().navigateUp()
        }
        chooseViewModel.selectedTrxCategory.observe(viewLifecycleOwner) {
            setNavigationResult(it?.id, Constants.TRX_CATEGORY_RESULT_KEY)
            findNavController().navigateUp()
        }
    }

    protected open fun setupRecycler() {
        val layoutMngr: RecyclerView.LayoutManager =
            GridLayoutManager(requireContext(), SPAN_COUNT)

        with(binding.accountRecycler) {
            layoutManager = layoutMngr
            adapter = imageRecyclerAdapter
        }
    }
}