package com.bogdanmurzin.mypersonalwallet.ui.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bogdanmurzin.domain.entities.Icon
import com.bogdanmurzin.mypersonalwallet.adapter.IconsRecyclerViewAdapter
import com.bogdanmurzin.mypersonalwallet.databinding.ActivityIconsBinding
import com.bogdanmurzin.mypersonalwallet.ui.presenter.IconsPresenter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class IconsActivity : AppCompatActivity(), IconsPresenter.IconView {

    private lateinit var binding: ActivityIconsBinding
    private lateinit var recyclerAdapter: IconsRecyclerViewAdapter

    @Inject
    lateinit var presenter: IconsPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // application.getComponent().iconComponent().create().inject(this)
        binding = ActivityIconsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecycler()

        presenter.attachView(this)
        presenter.search(binding.search)
    }

    private fun setupRecycler() {
        val layoutManager: RecyclerView.LayoutManager = GridLayoutManager(this, SPAN_COUNT)
        recyclerAdapter = IconsRecyclerViewAdapter()
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