package com.bogdanmurzin.mypersonalwallet.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.coroutineScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bogdanmurzin.domain.entities.AccountType
import com.bogdanmurzin.domain.entities.CategoryEntity
import com.bogdanmurzin.mypersonalwallet.R
import com.bogdanmurzin.mypersonalwallet.adapter.ImageRecyclerViewAdapter
import com.bogdanmurzin.mypersonalwallet.common.Constants.TAG
import com.bogdanmurzin.mypersonalwallet.databinding.DialogAccountChooseBinding
import com.bogdanmurzin.mypersonalwallet.ui.viewmodel.AddTransactionViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AccountChooseDialogFragment : DialogFragment() {

    private lateinit var binding: DialogAccountChooseBinding
    private lateinit var recyclerAdapter: ImageRecyclerViewAdapter
    private val viewModel: AddTransactionViewModel by navGraphViewModels(R.id.add_transaction_flow_graph) {
        defaultViewModelProviderFactory
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogAccountChooseBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecycler()
        lifecycle.coroutineScope.launch {
            viewModel.getAllAccounts().collect {
                recyclerAdapter.submitList(it)
            }
        }
    }

    private fun setupRecycler() {
        val layoutManager: RecyclerView.LayoutManager =
            GridLayoutManager(requireContext(), SPAN_COUNT)
        recyclerAdapter = ImageRecyclerViewAdapter {
            lifecycle.coroutineScope.launch {
                val accountId = viewModel.getAccountId(it as AccountType)
                viewModel.getAccount(accountId)

                findNavController().navigateUp()
            }
        }
        val recyclerView = binding.accountRecycler
        recyclerView.adapter = recyclerAdapter
        recyclerView.layoutManager = layoutManager
    }

    fun testIconList(): List<CategoryEntity> =
        listOf(
            AccountType("title1", "https://cdn4.iconfinder.com/data/icons/48-bubbles/48/07.Wallet-48.png"),
            AccountType("title2", "https://cdn4.iconfinder.com/data/icons/48-bubbles/48/07.Wallet-48.png"),
            AccountType("title3", "https://cdn4.iconfinder.com/data/icons/48-bubbles/48/07.Wallet-48.png"),
            AccountType("title4", "https://cdn4.iconfinder.com/data/icons/48-bubbles/48/07.Wallet-48.png"),
            AccountType("title5", "https://cdn4.iconfinder.com/data/icons/48-bubbles/48/07.Wallet-48.png"),
            AccountType("title6", "https://cdn4.iconfinder.com/data/icons/48-bubbles/48/07.Wallet-48.png"),
            AccountType("title7", "https://cdn4.iconfinder.com/data/icons/48-bubbles/48/07.Wallet-48.png"),
            AccountType("title8", "https://cdn4.iconfinder.com/data/icons/48-bubbles/48/07.Wallet-48.png"),
            AccountType("title9", "https://cdn4.iconfinder.com/data/icons/48-bubbles/48/07.Wallet-48.png"),
            AccountType("title10", "https://cdn4.iconfinder.com/data/icons/48-bubbles/48/07.Wallet-48.png"),
            AccountType("title11", "https://cdn4.iconfinder.com/data/icons/48-bubbles/48/07.Wallet-48.png"),
            AccountType("title12", "https://cdn4.iconfinder.com/data/icons/48-bubbles/48/07.Wallet-48.png"),
            AccountType("title13", "https://cdn4.iconfinder.com/data/icons/48-bubbles/48/07.Wallet-48.png"),
            AccountType("title14", "https://cdn4.iconfinder.com/data/icons/48-bubbles/48/07.Wallet-48.png"),
            AccountType("title15", "https://cdn4.iconfinder.com/data/icons/48-bubbles/48/07.Wallet-48.png"),
            AccountType("title16", "https://cdn4.iconfinder.com/data/icons/48-bubbles/48/07.Wallet-48.png"),
            AccountType("title17", "https://cdn4.iconfinder.com/data/icons/48-bubbles/48/07.Wallet-48.png"),
            AccountType("title18", "https://cdn4.iconfinder.com/data/icons/48-bubbles/48/07.Wallet-48.png"),
            AccountType("title19", "https://cdn4.iconfinder.com/data/icons/48-bubbles/48/07.Wallet-48.png"),
            AccountType("title20", "https://cdn4.iconfinder.com/data/icons/48-bubbles/48/07.Wallet-48.png"),
            AccountType("title21", "https://cdn4.iconfinder.com/data/icons/aiga-symbol-signs/388/aiga_taxi-48.png"),
            AccountType("title22", "https://cdn4.iconfinder.com/data/icons/aiga-symbol-signs/388/aiga_taxi-48.png"),
            AccountType("title23", "https://cdn4.iconfinder.com/data/icons/aiga-symbol-signs/388/aiga_taxi-48.png"),
            AccountType("title24", "https://cdn4.iconfinder.com/data/icons/aiga-symbol-signs/388/aiga_taxi-48.png"),
            AccountType("title25", "https://cdn4.iconfinder.com/data/icons/aiga-symbol-signs/388/aiga_taxi-48.png"),
            AccountType("title26", "https://cdn4.iconfinder.com/data/icons/aiga-symbol-signs/388/aiga_taxi-48.png"),
            AccountType("title27", "https://cdn4.iconfinder.com/data/icons/aiga-symbol-signs/388/aiga_taxi-48.png"),
            AccountType("title28", "https://cdn4.iconfinder.com/data/icons/aiga-symbol-signs/388/aiga_taxi-48.png"),
            AccountType("title29", "https://cdn4.iconfinder.com/data/icons/aiga-symbol-signs/388/aiga_taxi-48.png"),
            AccountType("title30", "https://cdn4.iconfinder.com/data/icons/aiga-symbol-signs/388/aiga_taxi-48.png")
        )

    companion object {
        const val SPAN_COUNT = 4
    }
}