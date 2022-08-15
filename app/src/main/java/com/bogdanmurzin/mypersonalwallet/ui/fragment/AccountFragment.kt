package com.bogdanmurzin.mypersonalwallet.ui.fragment

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
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
import com.google.android.material.floatingactionbutton.FloatingActionButton
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
        landscapeConfigure()
        return binding.root
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

    private fun landscapeConfigure() {
        // Whe have fab in the rail view
        activity?.let { act ->
            if (act.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {

                val railFab = act.findViewById<FloatingActionButton>(R.id.fab)
                railFab.setOnClickListener {
                    // TODO create account
                }
                val detailsContainer =
                    act.findViewById<FragmentContainerView>(R.id.details_fragment_container)
                detailsContainer.visibility = View.VISIBLE
                toolbar?.menu?.findItem(R.id.m_add)?.isVisible = false
            }
        }
    }
}