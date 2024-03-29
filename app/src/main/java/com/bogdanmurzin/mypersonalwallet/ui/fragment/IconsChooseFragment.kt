package com.bogdanmurzin.mypersonalwallet.ui.fragment

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bogdanmurzin.domain.entities.Icon
import com.bogdanmurzin.mypersonalwallet.R
import com.bogdanmurzin.mypersonalwallet.adapter.IconsRecyclerViewAdapter
import com.bogdanmurzin.mypersonalwallet.common.Constants.SPAN_COUNT
import com.bogdanmurzin.mypersonalwallet.databinding.FragemntIconsBinding
import com.bogdanmurzin.mypersonalwallet.ui.presenter.IconsPresenter
import com.bogdanmurzin.mypersonalwallet.ui.viewmodel.AddAccountViewModel
import com.bogdanmurzin.mypersonalwallet.ui.viewmodel.AddTrxCategoryViewModel
import com.bogdanmurzin.mypersonalwallet.util.CategoryArg
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class IconsChooseFragment : DialogFragment(), IconsPresenter.IconView {

    private lateinit var binding: FragemntIconsBinding
    private lateinit var recyclerAdapter: IconsRecyclerViewAdapter
    private val accountViewModel: AddAccountViewModel by navGraphViewModels(R.id.add_account_flow_graph) {
        defaultViewModelProviderFactory
    }
    private val trxCategoryViewModel: AddTrxCategoryViewModel by navGraphViewModels(R.id.add_trx_category_flow_graph) {
        defaultViewModelProviderFactory
    }
    private val args: IconsChooseFragmentArgs by navArgs()

    @Inject
    lateinit var presenter: IconsPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, com.google.android.material.R.style.ThemeOverlay_MaterialComponents)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragemntIconsBinding.inflate(layoutInflater)
        presenter.attachView(this)
        presenter.search(binding.search)

        setupRecycler()

        binding.cancelIv.setOnClickListener {
            findNavController().navigateUp()
        }
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        val dialog: Dialog? = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window?.setLayout(width, height)
        }
    }

    private fun setupRecycler() {
        val layoutManager: RecyclerView.LayoutManager =
            GridLayoutManager(requireContext(), SPAN_COUNT)
        recyclerAdapter = IconsRecyclerViewAdapter {
            if (args.category == CategoryArg.ACCOUNT_TYPE) {
                accountViewModel.setImageUrl(it.preview)
            } else {
                trxCategoryViewModel.setImageUrl(it.preview)
            }
            findNavController().navigateUp()
        }
        val recyclerView = binding.recycler
        recyclerView.adapter = recyclerAdapter
        recyclerView.layoutManager = layoutManager
    }

    override fun showIcons(list: List<Icon>) {
        recyclerAdapter.submitList(list)
    }

    override fun showMessageTv(message: String?) {
        binding.nothingMessageTv.text = message
        binding.nothingMessageTv.visibility = View.VISIBLE
    }

    override fun hideMessageTv() {
        binding.nothingMessageTv.visibility = View.INVISIBLE
    }

    override fun showRecycler() {
        binding.recycler.visibility = View.VISIBLE
    }

    override fun hideRecycler() {
        binding.recycler.visibility = View.INVISIBLE
    }

    override fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        binding.progressBar.visibility = View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
        presenter.clearCompositeDisposable()
    }

}