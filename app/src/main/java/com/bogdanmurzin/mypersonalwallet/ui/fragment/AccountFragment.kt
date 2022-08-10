package com.bogdanmurzin.mypersonalwallet.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bogdanmurzin.mypersonalwallet.R
import com.bogdanmurzin.mypersonalwallet.adapter.ImageRecyclerViewAdapter
import com.bogdanmurzin.mypersonalwallet.common.Constants.SPAN_COUNT
import com.bogdanmurzin.mypersonalwallet.databinding.FragmentAccountBinding
import com.bogdanmurzin.mypersonalwallet.ui.viewmodel.AccountViewModel
import com.bogdanmurzin.mypersonalwallet.util.Event
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccountFragment : Fragment() {

    private lateinit var binding: FragmentAccountBinding
    private lateinit var imageRecyclerAdapter: ImageRecyclerViewAdapter

    private val viewModel: AccountViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAccountBinding.inflate(layoutInflater)
        with(binding.accountTypeLayout.toolbar) {
            inflateMenu(R.menu.add_menu)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.m_add -> {
                        add()
                        true
                    }
                    else -> false
                }
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        observeViewModel()
        setupRecycler()
    }

    private fun observeViewModel() {
        // show all user account types
        viewModel.accountTypes.observe(viewLifecycleOwner) {
            imageRecyclerAdapter.submitList(it)
        }
        // Open bottom sheet
        viewModel.action.observe(viewLifecycleOwner) {
            findNavController().navigate(it)
        }
    }

    private fun setupRecycler() {
        val layoutMngr: RecyclerView.LayoutManager =
            GridLayoutManager(requireContext(), SPAN_COUNT)

        imageRecyclerAdapter = ImageRecyclerViewAdapter {
            // Create dialog with editing Account
            viewModel.openBottomSheet(Event.OpenPreviewScreen(it.id))
        }
        with(binding.accountTypeLayout.categoryRecycler) {
            layoutManager = layoutMngr
            adapter = imageRecyclerAdapter
        }
    }

    private fun add() {
        viewModel.openBottomSheet(Event.OpenPreviewScreen(0))
    }

}