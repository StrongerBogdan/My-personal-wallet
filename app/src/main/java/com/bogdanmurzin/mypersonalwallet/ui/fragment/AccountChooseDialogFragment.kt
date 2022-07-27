package com.bogdanmurzin.mypersonalwallet.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.coroutineScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bogdanmurzin.data.entity.AccountTypeEntity
import com.bogdanmurzin.data.entity.CategoryEntity
import com.bogdanmurzin.mypersonalwallet.R
import com.bogdanmurzin.mypersonalwallet.adapter.ImageRecyclerViewAdapter
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
        recyclerAdapter.submitList(testIconList())
    }

    private fun setupRecycler() {
        val layoutManager: RecyclerView.LayoutManager =
            GridLayoutManager(requireContext(), SPAN_COUNT)
        recyclerAdapter = ImageRecyclerViewAdapter {
            lifecycle.coroutineScope.launch {
                viewModel.getAccount(it.id)
            }

            findNavController().navigateUp()
        }
        val recyclerView = binding.accountRecycler
        recyclerView.adapter = recyclerAdapter
        recyclerView.layoutManager = layoutManager
    }

    fun testIconList(): List<CategoryEntity> =
        listOf(
            AccountTypeEntity(1,"title1", "https://cdn4.iconfinder.com/data/icons/48-bubbles/48/07.Wallet-48.png"),
            AccountTypeEntity(2,"title2", "https://cdn4.iconfinder.com/data/icons/48-bubbles/48/07.Wallet-48.png"),
            AccountTypeEntity(3,"title3", "https://cdn4.iconfinder.com/data/icons/48-bubbles/48/07.Wallet-48.png"),
            AccountTypeEntity(4,"title4", "https://cdn4.iconfinder.com/data/icons/48-bubbles/48/07.Wallet-48.png"),
            AccountTypeEntity(5,"title5", "https://cdn4.iconfinder.com/data/icons/48-bubbles/48/07.Wallet-48.png"),
            AccountTypeEntity(6,"title6", "https://cdn4.iconfinder.com/data/icons/48-bubbles/48/07.Wallet-48.png"),
            AccountTypeEntity(7,"title7", "https://cdn4.iconfinder.com/data/icons/48-bubbles/48/07.Wallet-48.png"),
            AccountTypeEntity(8,"title8", "https://cdn4.iconfinder.com/data/icons/48-bubbles/48/07.Wallet-48.png"),
            AccountTypeEntity(9,"title9", "https://cdn4.iconfinder.com/data/icons/48-bubbles/48/07.Wallet-48.png"),
            AccountTypeEntity(10,"title10", "https://cdn4.iconfinder.com/data/icons/48-bubbles/48/07.Wallet-48.png"),
            AccountTypeEntity(11,"title11", "https://cdn4.iconfinder.com/data/icons/48-bubbles/48/07.Wallet-48.png"),
            AccountTypeEntity(12,"title12", "https://cdn4.iconfinder.com/data/icons/48-bubbles/48/07.Wallet-48.png"),
            AccountTypeEntity(13,"title13", "https://cdn4.iconfinder.com/data/icons/48-bubbles/48/07.Wallet-48.png"),
            AccountTypeEntity(14,"title14", "https://cdn4.iconfinder.com/data/icons/48-bubbles/48/07.Wallet-48.png"),
            AccountTypeEntity(15,"title15", "https://cdn4.iconfinder.com/data/icons/48-bubbles/48/07.Wallet-48.png"),
            AccountTypeEntity(16,"title16", "https://cdn4.iconfinder.com/data/icons/48-bubbles/48/07.Wallet-48.png"),
            AccountTypeEntity(17,"title17", "https://cdn4.iconfinder.com/data/icons/48-bubbles/48/07.Wallet-48.png"),
            AccountTypeEntity(18,"title18", "https://cdn4.iconfinder.com/data/icons/48-bubbles/48/07.Wallet-48.png"),
            AccountTypeEntity(19,"title19", "https://cdn4.iconfinder.com/data/icons/48-bubbles/48/07.Wallet-48.png"),
            AccountTypeEntity(20,"title20", "https://cdn4.iconfinder.com/data/icons/48-bubbles/48/07.Wallet-48.png"),
            AccountTypeEntity(21,"title21", "https://cdn4.iconfinder.com/data/icons/aiga-symbol-signs/388/aiga_taxi-48.png"),
            AccountTypeEntity(22,"title22", "https://cdn4.iconfinder.com/data/icons/aiga-symbol-signs/388/aiga_taxi-48.png"),
            AccountTypeEntity(23,"title23", "https://cdn4.iconfinder.com/data/icons/aiga-symbol-signs/388/aiga_taxi-48.png"),
            AccountTypeEntity(24,"title24", "https://cdn4.iconfinder.com/data/icons/aiga-symbol-signs/388/aiga_taxi-48.png"),
            AccountTypeEntity(25,"title25", "https://cdn4.iconfinder.com/data/icons/aiga-symbol-signs/388/aiga_taxi-48.png"),
            AccountTypeEntity(26,"title26", "https://cdn4.iconfinder.com/data/icons/aiga-symbol-signs/388/aiga_taxi-48.png"),
            AccountTypeEntity(27,"title27", "https://cdn4.iconfinder.com/data/icons/aiga-symbol-signs/388/aiga_taxi-48.png"),
            AccountTypeEntity(28,"title28", "https://cdn4.iconfinder.com/data/icons/aiga-symbol-signs/388/aiga_taxi-48.png"),
            AccountTypeEntity(29,"title29", "https://cdn4.iconfinder.com/data/icons/aiga-symbol-signs/388/aiga_taxi-48.png"),
            AccountTypeEntity(30,"title30", "https://cdn4.iconfinder.com/data/icons/aiga-symbol-signs/388/aiga_taxi-48.png")
        )

    interface AccountChooseListener {
        fun onClose(id: Int)
    }

    companion object {
        const val SPAN_COUNT = 4
    }
}