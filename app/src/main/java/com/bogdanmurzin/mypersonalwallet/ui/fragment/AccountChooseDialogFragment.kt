package com.bogdanmurzin.mypersonalwallet.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.coroutineScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bogdanmurzin.domain.entities.AccountType
import com.bogdanmurzin.domain.entities.CategoryEntity
import com.bogdanmurzin.domain.entities.TransactionCategory
import com.bogdanmurzin.mypersonalwallet.R
import com.bogdanmurzin.mypersonalwallet.adapter.ImageRecyclerViewAdapter
import com.bogdanmurzin.mypersonalwallet.databinding.DialogCategoryChooseBinding
import com.bogdanmurzin.mypersonalwallet.ui.viewmodel.AddTransactionViewModel
import com.bogdanmurzin.mypersonalwallet.util.CategoryArg
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AccountChooseDialogFragment : DialogFragment() {

    private lateinit var binding: DialogCategoryChooseBinding
    private lateinit var imageRecyclerAdapter: ImageRecyclerViewAdapter
    private val viewModel: AddTransactionViewModel by navGraphViewModels(R.id.add_transaction_flow_graph) {
        defaultViewModelProviderFactory
    }
    private val args: AccountChooseDialogFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogCategoryChooseBinding.inflate(layoutInflater)
        binding.titleAccountChoose.text =
            if (args.category == CategoryArg.ACCOUNT_TYPE)
                resources.getString(R.string.choose_account)
            else
                resources.getString(R.string.choose_category)

        binding.doneBtn.setOnClickListener {
            val selectedCategoryTitle = viewModel.selectedCategoryTitle

            if (selectedCategoryTitle == null) {
                findNavController().navigateUp()
                return@setOnClickListener
            }

            viewModel.selectTransactionCategory(selectedCategoryTitle)
            findNavController().navigateUp()

        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // On which cardView user clicked
        // Account type cardView
        if (args.category == CategoryArg.ACCOUNT_TYPE) {
            binding.doneBtn.visibility = View.GONE
            setupRecycler {
                viewModel.selectAccountType(it as AccountType)
                findNavController().navigateUp()
            }
            // Get All user accounts and show them
            lifecycle.coroutineScope.launch {
                viewModel.getAllAccounts().collect {
                    imageRecyclerAdapter.submitList(it)
                }
            }
            // Transaction Category cardView
        } else {
            // setup main recycler
            setupRecycler {
                lifecycle.coroutineScope.launch {
                    // Remove selected subcategory
                    viewModel.selectedSubcategoryTitle = null
                    // "trx" it's short form of "transaction"
                    val trxCategoryId = viewModel.getTrxCategoryId(it as TransactionCategory)
                    viewModel.getAllTrxSubCategories(trxCategoryId).collect { list ->
                        if (list.isNotEmpty()) {
                            binding.subcategoriesScroll.visibility = View.VISIBLE
                            with(binding.subcategoryChipGroup) {
                                removeAllViews()
                                list.forEach { category ->
                                    addChip(category.subcategory)
                                }
                                isSingleSelection = true

                                setOnCheckedStateChangeListener { group, checkedList ->
                                    // There is always one element
                                    val chip: Chip = group.findViewById(checkedList[0])
                                    viewModel.selectedSubcategoryTitle = chip.text.toString()
                                }
                            }
                        } else {
                            // If transaction category has no subcategories
                            binding.subcategoriesScroll.visibility = View.GONE
                        }
                    }
                }
            }
            // Get All user transaction categories and show them
            lifecycle.coroutineScope.launch {
                viewModel.getAllTrxCategories().collect {
                    imageRecyclerAdapter.submitList(it)
                }
            }
        }
    }

    private fun ChipGroup.addChip(label: String?) {
        val chip = layoutInflater.inflate(R.layout.layout_chip_choice, this, false) as Chip
        chip.apply {
            text = label
            id = View.generateViewId()
            addView(this)
        }
    }

    private fun setupRecycler(onItemClicked: (CategoryEntity) -> Unit) {
        val layoutMngr: RecyclerView.LayoutManager =
            GridLayoutManager(requireContext(), SPAN_COUNT)

        imageRecyclerAdapter = ImageRecyclerViewAdapter(onItemClicked)
        with(binding.accountRecycler) {
            layoutManager = layoutMngr
            adapter = imageRecyclerAdapter
        }
    }

    companion object {
        const val SPAN_COUNT = 4
    }
}