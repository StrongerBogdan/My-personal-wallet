package com.bogdanmurzin.mypersonalwallet.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
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
import com.google.android.material.appbar.MaterialToolbar
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AccountFragment : CategoryFragment() {

    private lateinit var binding: FragmentAccountBinding

    private val viewModel: AccountViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentAccountBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val toolbar: MaterialToolbar? = activity?.findViewById(R.id.toolbar)
        toolbar?.title = getString(R.string.account_type)
    }

    override fun observeViewModel() {
        // show all user account types
        viewModel.accountTypes.observe(viewLifecycleOwner) {
            imageRecyclerAdapter.submitList(it)
        }
        // Open bottom sheet
        viewModel.action.observe(viewLifecycleOwner) {
            findNavController().navigate(it)
        }
    }

    override fun setupRecycler() {
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

    override fun add() {
        viewModel.openBottomSheet(Event.OpenPreviewScreen(0))
    }
}