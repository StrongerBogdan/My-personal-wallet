package com.bogdanmurzin.mypersonalwallet.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bogdanmurzin.mypersonalwallet.R
import com.bogdanmurzin.mypersonalwallet.adapter.MyMoneyTransactionRecyclerViewAdapter
import com.bogdanmurzin.mypersonalwallet.databinding.FragmentMoneyTransactionsListBinding
import com.bogdanmurzin.mypersonalwallet.ui.viewmodel.MainViewModel
import com.bogdanmurzin.mypersonalwallet.util.Event
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class FragmentMoneyTransactions : Fragment() {

    private lateinit var binding: FragmentMoneyTransactionsListBinding
    private lateinit var recyclerAdapter: MyMoneyTransactionRecyclerViewAdapter

    private val viewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMoneyTransactionsListBinding.inflate(layoutInflater)
        binding.fab.setOnClickListener {
            viewModel.openBottomSheet(Event.OpenPreviewScreen(0))
        }

        viewModel.transactionsList.observe(viewLifecycleOwner) {
            recyclerAdapter.submitList(it)
        }

        viewModel.action.observe(viewLifecycleOwner) {
            findNavController().navigate(it)
        }

        return binding.root
    }

    private fun setupRecycler() {
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(requireContext())
        recyclerAdapter = MyMoneyTransactionRecyclerViewAdapter({
            // Create dialog with editing Transaction
            viewModel.openBottomSheet(Event.OpenPreviewScreen(it.id))
        }, { show ->
            // Update toolbar (show/unshow delete icon)
            updateToolbar(show)
            viewModel.isDeleteEnabled = show
        }, { selectedList ->
            viewModel.selectedTransactionsIds = selectedList.map { it.id }
        })
        val recyclerView = binding.transactionRecycler
        recyclerView.adapter = recyclerAdapter
        recyclerView.layoutManager = layoutManager
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecycler()
        // Update recycler when we return from another tab
        viewModel.updateTransactions()

        binding.toolbar.inflateMenu(R.menu.delete_menu)
        updateToolbar(viewModel.isDeleteEnabled)

        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.m_delete -> {
                    delete()

                    viewModel.isDeleteEnabled = true
                    viewModel.isDeleteEnabled
                }
                else -> {
                    viewModel.isDeleteEnabled = false
                    viewModel.isDeleteEnabled
                }
            }
        }
    }

    private fun delete() {
        val alertDialog = AlertDialog.Builder(requireContext())
            .setTitle("Delete")
            .setMessage("Do you want to delete the transactions")
            .setPositiveButton("Delete") { _, _ ->
                val selectedList =
                    recyclerAdapter.currentList.filter { it.isSelected }.map { it.id }
                viewModel.deleteTransactions(selectedList)
                viewModel.isDeleteEnabled = false
                recyclerAdapter.isEnabledDeleting = viewModel.isDeleteEnabled
                updateToolbar(viewModel.isDeleteEnabled)
            }
            .setNegativeButton("Cancel") { _, _ -> }
        alertDialog.show()
    }

    private fun updateToolbar(show: Boolean) {
        val saveItem = binding.toolbar.menu.findItem(R.id.m_delete)
        saveItem.isVisible = show
    }
}