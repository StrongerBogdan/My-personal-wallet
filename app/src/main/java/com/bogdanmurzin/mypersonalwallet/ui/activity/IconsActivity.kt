package com.bogdanmurzin.mypersonalwallet.ui.activity

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bogdanmurzin.domain.entities.Icon
import com.bogdanmurzin.mypersonalwallet.R
import com.bogdanmurzin.mypersonalwallet.adapter.IconsRecyclerViewAdapter
import com.bogdanmurzin.mypersonalwallet.databinding.ActivityIconsBinding
import com.bogdanmurzin.mypersonalwallet.ui.presenter.IconsPresenter
import com.bogdanmurzin.mypersonalwallet.ui.viewmodel.AddAccountViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class IconsActivity : DialogFragment(), IconsPresenter.IconView {

    private lateinit var binding: ActivityIconsBinding
    private lateinit var recyclerAdapter: IconsRecyclerViewAdapter
    private val viewModel: AddAccountViewModel by navGraphViewModels(R.id.add_account_flow_graph) {
        defaultViewModelProviderFactory
    }

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
        binding = ActivityIconsBinding.inflate(layoutInflater)
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
            viewModel.setImageUrl(it.preview)
            findNavController().navigateUp()


        }
        val recyclerView = binding.recycler
        recyclerView.adapter = recyclerAdapter
        recyclerView.layoutManager = layoutManager
    }

    override fun showIcons(list: List<Icon>) {
        recyclerAdapter.submitList(list)
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

    companion object {
        const val SPAN_COUNT = 5
    }
}