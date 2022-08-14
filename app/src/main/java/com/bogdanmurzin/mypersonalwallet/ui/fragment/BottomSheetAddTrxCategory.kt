package com.bogdanmurzin.mypersonalwallet.ui.fragment

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import com.bogdanmurzin.mypersonalwallet.R
import com.bogdanmurzin.mypersonalwallet.common.Constants
import com.bogdanmurzin.mypersonalwallet.databinding.FragmentBottomsheetAddTrxcategoryBinding
import com.bogdanmurzin.mypersonalwallet.ui.viewmodel.AddTrxCategoryViewModel
import com.bogdanmurzin.mypersonalwallet.util.CategoryArg
import com.bogdanmurzin.mypersonalwallet.util.EditingState
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BottomSheetAddTrxCategory : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentBottomsheetAddTrxcategoryBinding
    private val viewModel: AddTrxCategoryViewModel by navGraphViewModels(R.id.add_trx_category_flow_graph) {
        defaultViewModelProviderFactory
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBottomsheetAddTrxcategoryBinding.inflate(inflater)
        binding.trxcategoryContainer.setOnClickListener {
            findNavController().navigate(
                BottomSheetAddTrxCategoryDirections
                    .actionBottomSheetAddTrxCategoryToIconsActivity(CategoryArg.TRANSACTION_CATEGORY)
            )

        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val args: BottomSheetAddTrxCategoryArgs by navArgs()

        val editingState =
            if (args.trxCategoryId > 0) EditingState.EXISTING_TRANSACTION
            else EditingState.NEW_TRANSACTION

        // If we arrived here with an itemId of >= 0, then we are editing an existing item
        if (editingState == EditingState.EXISTING_TRANSACTION) {
            viewModel.setUpData(args.trxCategoryId)
            binding.subcategoriesScroll.visibility = View.VISIBLE
        }

        // When the user clicks the Done button, use the data here to either update
        // an existing item or create a new one
        binding.doneBtn.setOnClickListener {
            if (viewModel.addNewTrxCategory(
                    args.trxCategoryId,
                    binding.trxcategoryCategory.text.toString(),
                    editingState
                )
            ) {
                // if validated and added/edited successfully
                findNavController().navigateUp()
            }

            // TODO add canceling dialog
        }

        setupViewModel()
    }

    private fun setupViewModel() {
        // Loaded Transaction categories for editing
        viewModel.loadedTrxCategories.observe(viewLifecycleOwner) { trxCategory ->
            binding.trxcategoryCategory.setText(trxCategory.title)
        }
        viewModel.currentImageUrl.observe(viewLifecycleOwner) { imageUrl ->
            if (imageUrl != null) {
                Glide.with(requireContext())
                    .load(Uri.parse(imageUrl))
                    .override(Constants.ICON_SCALE, Constants.ICON_SCALE)
                    .into(binding.trxcategoryIv.ivIcon)
            }
        }
        // Load all subCategories selected category
        viewModel.trxSubCategories.observe(viewLifecycleOwner) { list ->
            with(binding.subcategoryChipGroup) {
                removeAllViews()
                // First chip will be add chip
                addAddChip()
                list.forEach { category ->
                    addChip(category.subcategory)
                }
                isSingleSelection = true

                setOnCheckedStateChangeListener { group, checkedList ->
                    // There is always one element
                    val chip: Chip = group.findViewById(checkedList[0])
                    // If chip has "+" icon? -> add new subcategory
                    if (chip.isChipIconVisible) {
                        findNavController().navigate(
                            BottomSheetAddTrxCategoryDirections
                                .actionBottomSheetAddTrxCategoryToAddSubcategoryFragment()
                        )
                    } else {
                        viewModel.selectSubcategory(chip.text.toString())
                    }
                }
            }
        }
    }

    private fun ChipGroup.addChip(label: String?) {
        val chip = layoutInflater.inflate(R.layout.layout_chip_choice, this, false) as Chip
        chip.apply {
            text = label
            isClickable = false
            isChipIconVisible = false
            id = View.generateViewId()
            addView(this)
        }
    }

    private fun ChipGroup.addAddChip() {
        val chip = layoutInflater.inflate(R.layout.layout_chip_choice, this, false) as Chip
        chip.apply {
            setChipIconResource(R.drawable.ic_baseline_add)
            isChipIconVisible = true
            id = View.generateViewId()
            addView(this)
        }
    }
}