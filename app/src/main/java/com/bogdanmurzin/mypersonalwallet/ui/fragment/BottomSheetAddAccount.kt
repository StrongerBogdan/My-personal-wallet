package com.bogdanmurzin.mypersonalwallet.ui.fragment

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import com.bogdanmurzin.mypersonalwallet.R
import com.bogdanmurzin.mypersonalwallet.common.Constants
import com.bogdanmurzin.mypersonalwallet.data.transaction_recycer_items.TransactionItemUiModel
import com.bogdanmurzin.mypersonalwallet.databinding.FragmentBottomsheetAddAccountBinding
import com.bogdanmurzin.mypersonalwallet.ui.viewmodel.AddAccountViewModel
import com.bogdanmurzin.mypersonalwallet.util.CategoryArg
import com.bogdanmurzin.mypersonalwallet.util.EditingState
import com.bogdanmurzin.mypersonalwallet.util.Extensions.onDone
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BottomSheetAddAccount : BottomSheetDialogFragment(), IBottomSheetAdd<BottomSheetAddAccountArgs> {

    private lateinit var binding: FragmentBottomsheetAddAccountBinding
    private val viewModel: AddAccountViewModel by navGraphViewModels(R.id.add_account_flow_graph) {
        defaultViewModelProviderFactory
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBottomsheetAddAccountBinding.inflate(inflater)
        binding.accountContainer.setOnClickListener {
            findNavController().navigate(
                BottomSheetAddAccountDirections
                    .actionBottomSheetAddAccountToIconsActivity(CategoryArg.ACCOUNT_TYPE)
            )

        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val args: BottomSheetAddAccountArgs by navArgs()

        val editingState =
            if (args.accountId > 0) EditingState.EXISTING_TRANSACTION
            else EditingState.NEW_TRANSACTION

        // If we arrived here with an itemId of >= 0, then we are editing an existing item
        if (editingState == EditingState.EXISTING_TRANSACTION) {
            viewModel.setUpData(args.accountId)
            // TODO delete btn
            binding.deleteBtn.visibility = View.VISIBLE
            binding.deleteBtn.setOnClickListener {
                delete(args.accountId)
            }
        }

        // When the user clicks the Done button, use the data here to either update
        // an existing item or create a new one
        binding.doneBtn.setOnClickListener {
            done(args, editingState)
        }
        binding.accountTitle.onDone {
            done(args, editingState)
        }

        setupViewModel()
    }

    override fun done(
        args: BottomSheetAddAccountArgs,
        editingState: EditingState
    ) {
        viewModel.addNewAccountType(
            args.accountId,
            binding.accountTitle.text.toString(),
            editingState
        )
    }

    private fun delete(id: Int) {
        val deleteString = requireContext().getString(R.string.delete)
        val messageString = requireContext().getString(R.string.delete_message)
        val cancelString = requireContext().getString(R.string.cancel)

        val alertDialog = AlertDialog.Builder(requireContext())
            .setTitle(deleteString)
            .setMessage(messageString)
            .setPositiveButton(deleteString) { _, _ ->
                viewModel.deleteAccountType(id)
                // close bottomSheet
                dismiss()
            }
            .setNegativeButton(cancelString) { _, _ -> }
        alertDialog.show()
    }

    override fun setupViewModel() {
        // Loaded AccountType for editing
        viewModel.loadedAccountType.observe(viewLifecycleOwner) { account ->
            binding.accountTitle.setText(account.title)
        }
        viewModel.currentImageUrl.observe(viewLifecycleOwner) { imageUrl ->
            if (imageUrl != null) {
                Glide.with(requireContext())
                    .load(Uri.parse(imageUrl))
                    .override(Constants.ICON_SCALE, Constants.ICON_SCALE)
                    .into(binding.accountIv.ivIcon)
            }
        }
        viewModel.doneAction.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                // if validated and added/edited successfully
                findNavController().navigateUp()
            }
            result.onFailure {
                Log.e(Constants.TAG, "setupViewModel: ${it.message} ")
                Toast.makeText(
                    requireContext(),
                    getString(R.string.fill_all_required_category),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}