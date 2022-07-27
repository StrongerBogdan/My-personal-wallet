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
import com.bogdanmurzin.mypersonalwallet.databinding.DialogAccountChooseBinding
import com.bogdanmurzin.mypersonalwallet.ui.viewmodel.AddTransactionViewModel
import com.bogdanmurzin.mypersonalwallet.util.CategoryArg
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AccountChooseDialogFragment : DialogFragment() {

    private lateinit var binding: DialogAccountChooseBinding
    private lateinit var recyclerAdapter: ImageRecyclerViewAdapter
    private val viewModel: AddTransactionViewModel by navGraphViewModels(R.id.add_transaction_flow_graph) {
        defaultViewModelProviderFactory
    }
    private val args: AccountChooseDialogFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogAccountChooseBinding.inflate(layoutInflater)
        binding.titleAccountChoose.text =
            if (args.category == CategoryArg.ACCOUNT_TYPE)
                resources.getString(R.string.choose_account)
            else
                resources.getString(R.string.choose_category)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // On which cardView user clicked
        if (args.category == CategoryArg.ACCOUNT_TYPE) {
            setupRecycler {
                lifecycle.coroutineScope.launch {
                    val accountId = viewModel.getAccountId(it as AccountType)
                    viewModel.getAccount(accountId)
                    findNavController().navigateUp()
                }
            }
            // Get All user accounts and show them
            lifecycle.coroutineScope.launch {
                viewModel.getAllAccounts().collect {
                    recyclerAdapter.submitList(it)
                }
            }
        } else {
            setupRecycler {
                lifecycle.coroutineScope.launch {
                    // "trx" it's short form of "transaction"
                    val trxCategoryId = viewModel.getTrxCategoryId(it as TransactionCategory)
                    viewModel.getTrxCategory(trxCategoryId)
                    findNavController().navigateUp()
                }
            }
            // Get All user transaction categories and show them
            lifecycle.coroutineScope.launch {
                viewModel.getAllTrxCategories().collect {
                    recyclerAdapter.submitList(it)
                }
            }
        }
    }

    private fun setupRecycler(onItemClicked: (CategoryEntity) -> Unit) {
        val layoutMngr: RecyclerView.LayoutManager =
            GridLayoutManager(requireContext(), SPAN_COUNT)

        recyclerAdapter = ImageRecyclerViewAdapter(onItemClicked)
        with(binding.accountRecycler) {
            layoutManager = layoutMngr
            adapter = recyclerAdapter
        }
    }

    companion object {
        const val SPAN_COUNT = 4
    }
}