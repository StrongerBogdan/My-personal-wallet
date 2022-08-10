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
import com.bogdanmurzin.mypersonalwallet.databinding.FragmentBottomsheetAddAccountBinding
import com.bogdanmurzin.mypersonalwallet.ui.viewmodel.AddAccountViewModel
import com.bogdanmurzin.mypersonalwallet.util.CategoryArg
import com.bogdanmurzin.mypersonalwallet.util.EditingState
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BottomSheetAddAccount : BottomSheetDialogFragment() {

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
        }

        // When the user clicks the Done button, use the data here to either update
        // an existing item or create a new one
        binding.doneBtn.setOnClickListener {
            if (viewModel.onBottomSheetDoneBtnClicked(
                    args.accountId,
                    binding.accountTitle.text.toString(),
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
    }
}