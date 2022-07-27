package com.bogdanmurzin.mypersonalwallet.ui.fragment

import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import com.bogdanmurzin.mypersonalwallet.R
import com.bogdanmurzin.mypersonalwallet.common.Constants
import com.bogdanmurzin.mypersonalwallet.common.Constants.TAG
import com.bogdanmurzin.mypersonalwallet.databinding.FragmentBottomsheetAddTransactionBinding
import com.bogdanmurzin.mypersonalwallet.ui.viewmodel.AddTransactionViewModel
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
            val navController = findNavController()
            // TODO: not yet implemented
        }

        // Account type
        viewModel.selectedAccountType.observe(viewLifecycleOwner) { account ->
            Log.i(TAG, "onViewCreated: ${account.title}")
            binding.accountTypeTv.text = account.title
            Glide.with(requireContext())
                .load(Uri.parse(account.imageUri))
                .override(Constants.ICON_SCALE, Constants.ICON_SCALE)
                .into(binding.accountTypeIv)
        }
    }

    private fun setupCardViews() {
        binding.accountCv.setOnClickListener {
            findNavController().navigate(R.id.action_bottomSheetAddTransaction_to_accountChooseDialogFragment)
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