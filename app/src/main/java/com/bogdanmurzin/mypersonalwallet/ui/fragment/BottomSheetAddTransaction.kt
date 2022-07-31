package com.bogdanmurzin.mypersonalwallet.ui.fragment

import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.lifecycle.coroutineScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import com.bogdanmurzin.domain.entities.Transaction
import com.bogdanmurzin.mypersonalwallet.R
import com.bogdanmurzin.mypersonalwallet.common.Constants
import com.bogdanmurzin.mypersonalwallet.databinding.FragmentBottomsheetAddTransactionBinding
import com.bogdanmurzin.mypersonalwallet.ui.viewmodel.AddTransactionViewModel
import com.bogdanmurzin.mypersonalwallet.util.CategoryArg
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.*

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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val args: BottomSheetAddTransactionArgs by navArgs()

        val editingState =
            if (args.tranasctionId > 0) EditingState.EXISTING_TRANSACTION
            else EditingState.NEW_TRANSACTION

        // If we arrived here with an itemId of >= 0, then we are editing an existing item
        if (editingState == EditingState.EXISTING_TRANSACTION) {
            TODO("not yet implemented editing")
        }

        // When the user clicks the Done button, use the data here to either update
        // an existing item or create a new one
        binding.doneBtn.setOnClickListener {
            lifecycle.coroutineScope.launch {
                val trxCategory = viewModel.selectedTrxCategory.value
                val accountType = viewModel.selectedAccountType.value
                val transactionAmount = binding.transactionAmountTv.text.toString().replace("$","")
                val description = binding.transactionDescription.text.toString().ifEmpty { null }

                if (trxCategory != null && accountType != null &&
                    transactionAmount.isNotEmpty() && transactionAmount.toFloat() != 0f
                ) {
                    val transaction = Transaction(
                        trxCategory,
                        // TODO Create date change
                        Calendar.getInstance().time,
                        description,
                        accountType,
                        transactionAmount.toFloat()
                    )
                    viewModel.addTransaction(transaction)
                    findNavController().navigateUp()
                }
                // TODO show dialog
            }
        }

        setupViewModel()
    }

    private fun setupViewModel() {
        // Account type
        viewModel.selectedAccountType.observe(viewLifecycleOwner) { account ->
            binding.accountTypeTv.text = account.title
            Glide.with(requireContext())
                .load(Uri.parse(account.imageUri))
                .override(Constants.ICON_SCALE, Constants.ICON_SCALE)
                .into(binding.accountTypeIv)
        }
        // Transaction category
        viewModel.selectedTrxCategory.observe(viewLifecycleOwner) { trxCategory ->
            binding.transactionCategoryTv.text = trxCategory.title
            binding.transactionSubcategoryTv.text =
                resources.getString(R.string.category_subtitle_template, trxCategory.subcategory)
            binding.transactionSubcategoryTv.visibility = View.VISIBLE
            if (trxCategory.subcategory.isNullOrEmpty()) binding.transactionSubcategoryTv.visibility =
                View.GONE
            Glide.with(requireContext())
                .load(Uri.parse(trxCategory.imageUri))
                .override(Constants.ICON_SCALE, Constants.ICON_SCALE)
                .into(binding.transactionCategoryIv)
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
                    val cleanString: String = s.replace("""[$,.]""".toRegex(), "")
                    val parsed = cleanString.toDouble()
                    val formatted = NumberFormat.getCurrencyInstance().format((parsed / 100))
                    textview.setText(formatted)
                    textview.setSelection(formatted.length)
                    textview.addTextChangedListener(this)
                }
            }
        })
    }

    private enum class EditingState {
        NEW_TRANSACTION,
        EXISTING_TRANSACTION
    }
}