package com.bogdanmurzin.mypersonalwallet.ui.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.coroutineScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bogdanmurzin.data.db.AppDatabase
import com.bogdanmurzin.data.mapper.AccountTypeEntityMapper
import com.bogdanmurzin.data.mapper.TransactionCategoryEntityMapper
import com.bogdanmurzin.data.mapper.TransactionsEntityMapper
import com.bogdanmurzin.data.repositories.TransactionRepositoryImpl
import com.bogdanmurzin.data.repositories.TransactionsLocalDataSource
import com.bogdanmurzin.data.repositories.TransactionsLocalDataSourceImpl
import com.bogdanmurzin.domain.usecases.GetTransactionsUseCase
import com.bogdanmurzin.mypersonalwallet.R
import com.bogdanmurzin.mypersonalwallet.adapter.MyMoneyTransactionRecyclerViewAdapter
import com.bogdanmurzin.mypersonalwallet.databinding.FragmentMoneyTransactionsListBinding
import com.bogdanmurzin.mypersonalwallet.mapper.TransactionUiMapper
import com.bogdanmurzin.mypersonalwallet.ui.viewmodel.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FragmentMoneyTransactions : Fragment() {

    private lateinit var thisContext: Context
    private lateinit var binding: FragmentMoneyTransactionsListBinding
    private lateinit var recyclerAdapter: MyMoneyTransactionRecyclerViewAdapter
    private val db by lazy { AppDatabase.getDatabase(requireContext()) }
    private val viewModel by viewModels<MainViewModel> {
        MainViewModel.Factory(
            GetTransactionsUseCase(
                TransactionRepositoryImpl(
                    TransactionsLocalDataSourceImpl(
                        db.transactionsDao(),
                        db.accountTypeDao(),
                        db.transactionCategoryDao(),
                        Dispatchers.IO,
                        TransactionsEntityMapper(
                            AccountTypeEntityMapper(), TransactionCategoryEntityMapper()
                        )
                    )
                )
            ),
            TransactionUiMapper()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMoneyTransactionsListBinding.inflate(layoutInflater)
        if (container != null) {
            thisContext = container.context
        }
        return binding.root
    }

    private fun setupRecycler() {
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(requireContext())
        recyclerAdapter = MyMoneyTransactionRecyclerViewAdapter {
            // Create dialog with editing Transaction
            Toast.makeText(
                thisContext,
                "You taped on ${it.description} transaction",
                Toast.LENGTH_SHORT
            ).show()
        }
        val recyclerView = binding.transactionRecycler
        recyclerView.adapter = recyclerAdapter
        recyclerView.layoutManager = layoutManager
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecycler()

        lifecycle.coroutineScope.launch {
            viewModel.updateRateList().collect {
                recyclerAdapter.submitList(it)
            }
        }
    }

}