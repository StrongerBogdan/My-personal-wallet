package com.bogdanmurzin.mypersonalwallet.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bogdanmurzin.domain.entities.AccountType
import com.bogdanmurzin.domain.entities.CategoryEntity
import com.bogdanmurzin.domain.entities.TransactionCategory
import com.bogdanmurzin.mypersonalwallet.R
import com.bogdanmurzin.mypersonalwallet.adapter.ImageRecyclerViewAdapter
import com.bogdanmurzin.mypersonalwallet.common.Constants
import com.bogdanmurzin.mypersonalwallet.databinding.DialogCategoryChooseBinding
import com.bogdanmurzin.mypersonalwallet.ui.viewmodel.CategoryChooseViewModel
import com.bogdanmurzin.mypersonalwallet.util.CategoryArg
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

class TrxCategoryChooseDialogFragment : CategoryChooseDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        binding.titleAccountChoose.text = resources.getString(R.string.choose_category)

        binding.doneBtn.setOnClickListener {
            chooseViewModel.chooseCategory()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        observeViewModel()
        setupRecycler()
        // Get All user transaction categories and show them
        chooseViewModel.showAllTrxCategories()
    }

    override fun observeViewModel() {
        super.observeViewModel()
        // show all user transaction categories
        chooseViewModel.trxCategories.observe(viewLifecycleOwner) {
            imageRecyclerAdapter.submitList(it)
        }
        // Load all subCategories selected category
        chooseViewModel.trxSubCategories.observe(viewLifecycleOwner) { list ->
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
                        //viewModel.selectSubcategory(chip.text.toString())
                        chooseViewModel.selectSubcategory(chip.text.toString())
                    }
                }
            } else {
                // If transaction category has no subcategories
                binding.subcategoriesScroll.visibility = View.GONE
            }
        }
    }

    override fun setupRecycler() {
        imageRecyclerAdapter = ImageRecyclerViewAdapter {
            // Remove selected subcategory
            chooseViewModel.selectSubcategory(null)
            // "trx" it's short form of "transaction"
            chooseViewModel.loadAllTrxSubCategories(it as TransactionCategory)
        }
        super.setupRecycler()
    }

    private fun ChipGroup.addChip(label: String?) {
        val chip = layoutInflater.inflate(R.layout.layout_chip_choice, this, false) as Chip
        chip.apply {
            text = label
            id = View.generateViewId()
            addView(this)
        }
    }
}