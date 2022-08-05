package com.bogdanmurzin.mypersonalwallet.ui.fragment

import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import com.bogdanmurzin.mypersonalwallet.R
import com.bogdanmurzin.mypersonalwallet.common.Constants
import com.bogdanmurzin.mypersonalwallet.common.Constants.DOLLAR_OR_COMA_OR_DOT_REGEX
import com.bogdanmurzin.mypersonalwallet.common.Constants.EMPTY_STRING
import com.bogdanmurzin.mypersonalwallet.databinding.FragmentBottomsheetAddTransactionBinding
import com.bogdanmurzin.mypersonalwallet.ui.viewmodel.AddTransactionViewModel
import com.bogdanmurzin.mypersonalwallet.util.CategoryArg
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import java.text.NumberFormat


@AndroidEntryPoint
class BottomSheetAddTransaction : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentBottomsheetAddTransactionBinding
    private val viewModel: AddTransactionViewModel by navGraphViewModels(R.id.add_transaction_flow_graph) {
        defaultViewModelProviderFactory
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBottomsheetAddTransactionBinding.inflate(inflater)
        setupEditText()
        setupCardViews()
        binding.datePicker.setOnClickListener {
            findNavController().navigate(
                BottomSheetAddTransactionDirections
                    .actionBottomSheetAddTransactionToDatePickerFragment()
            )
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val args: BottomSheetAddTransactionArgs by navArgs()

        val editingState =
            if (args.transactionId > 0) EditingState.EXISTING_TRANSACTION
            else EditingState.NEW_TRANSACTION

        // If we arrived here with an itemId of >= 0, then we are editing an existing item
        if (editingState == EditingState.EXISTING_TRANSACTION) {
            viewModel.setUpData(args.transactionId)
        }

        // When the user clicks the Done button, use the data here to either update
        // an existing item or create a new one
        binding.doneBtn.setOnClickListener {
            if (viewModel.onBottomSheetDoneBtnClicked(
                    args.transactionId,
                    binding.transactionAmountTv.text.toString(),
                    binding.transactionDescription.text.toString(),
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
        // Loaded Transaction for editing
        viewModel.loadedTransaction.observe(viewLifecycleOwner) { transactionUiModel ->
            binding.transactionAmountTv.setText(transactionUiModel.transactionAmount)
            binding.transactionDescription.setText(transactionUiModel.description)
        }
        // When changed account type (user chose one)
        viewModel.selectedAccountType.observe(viewLifecycleOwner) { account ->
            binding.accountType.categoryEntitySubcategoryTv.visibility = View.GONE
            binding.accountType.categoryEntityTitleTv.text = account.title
            Glide.with(requireContext())
                .load(Uri.parse(account.imageUri))
                .override(Constants.ICON_SCALE, Constants.ICON_SCALE)
                .into(binding.accountType.categoryEntityIv)
        }
        // When changed transaction category (user chose one)
        viewModel.selectedTrxCategory.observe(viewLifecycleOwner) { trxCategory ->
            binding.transactionCategoryType.categoryEntityTitleTv.text = trxCategory.title
            binding.transactionCategoryType.categoryEntitySubcategoryTv.text =
                resources.getString(R.string.category_subtitle_template, trxCategory.subcategory)
            binding.transactionCategoryType.categoryEntitySubcategoryTv.visibility = View.VISIBLE
            if (trxCategory.subcategory.isNullOrEmpty()) binding.transactionCategoryType.categoryEntitySubcategoryTv.visibility =
                View.GONE
            Glide.with(requireContext())
                .load(Uri.parse(trxCategory.imageUri))
                .override(Constants.ICON_SCALE, Constants.ICON_SCALE)
                .into(binding.transactionCategoryType.categoryEntityIv)
        }
        // When changed date
        viewModel.selectedDate.observe(viewLifecycleOwner) { date ->
            val dateFormat =
                DateFormat.getDateFormat(requireContext().applicationContext)
            binding.datePicker.text = dateFormat.format(date)
        }
    }

    private fun setupCardViews() {
        binding.accountCv.setOnClickListener {
            findNavController().navigate(
                BottomSheetAddTransactionDirections
                    .actionBottomSheetAddTransactionToAccountChooseDialogFragment(
                        CategoryArg.ACCOUNT_TYPE
                    )
            )
        }

        binding.transactionCv.setOnClickListener {
            findNavController().navigate(
                BottomSheetAddTransactionDirections
                    .actionBottomSheetAddTransactionToAccountChooseDialogFragment(
                        CategoryArg.TRANSACTION_CATEGORY
                    )
            )
        }
    }

    private fun setupEditText() {
        val textview = binding.transactionAmountTv
        textview.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) =
                Unit

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit

            override fun afterTextChanged(s: Editable?) {
                if (s != null) {
                    textview.removeTextChangedListener(this)
                    val cleanString: String = s.replace(DOLLAR_OR_COMA_OR_DOT_REGEX, EMPTY_STRING)
                    val parsed = cleanString.toDouble()
                    val formatted = NumberFormat.getCurrencyInstance().format((parsed / 100))
                    textview.setText(formatted)
                    textview.setSelection(formatted.length)
                    textview.addTextChangedListener(this)
                }
            }
        })
    }

    enum class EditingState {
        NEW_TRANSACTION,
        EXISTING_TRANSACTION
    }
}