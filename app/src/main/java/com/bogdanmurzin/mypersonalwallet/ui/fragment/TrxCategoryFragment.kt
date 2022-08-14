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
import com.bogdanmurzin.mypersonalwallet.databinding.FragmentCategoryBinding
import com.bogdanmurzin.mypersonalwallet.ui.viewmodel.TrxCategoryViewModel
import com.bogdanmurzin.mypersonalwallet.util.Event
import com.google.android.material.appbar.MaterialToolbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TrxCategoryFragment : Fragment() {

    private lateinit var binding: FragmentCategoryBinding
    private lateinit var imageRecyclerAdapter: ImageRecyclerViewAdapter
    private var toolbar: MaterialToolbar? = null

    private val viewModel: TrxCategoryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCategoryBinding.inflate(layoutInflater)
        toolbar = activity?.findViewById(R.id.toolbar)
        toolbar?.let { toolbar ->
            updateToolbar(true)
            toolbar.setOnMenuItemClickListener {
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
        viewModel.trxCategories.observe(viewLifecycleOwner) {
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
        with(binding.transactionCategoryLayout.categoryRecycler) {
            layoutManager = layoutMngr
            adapter = imageRecyclerAdapter
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        updateToolbar(false)
    }

    private fun add() {
        viewModel.openBottomSheet(Event.OpenPreviewScreen(0))
    }

    fun updateToolbar(isShowAddIcon: Boolean) {
        toolbar?.let {
            it.menu.findItem(R.id.m_add).isVisible = isShowAddIcon
            it.menu.findItem(R.id.m_delete).isVisible = !isShowAddIcon
            it.menu.findItem(R.id.m_settings).isVisible = !isShowAddIcon
        }
    }
}