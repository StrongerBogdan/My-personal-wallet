package com.bogdanmurzin.mypersonalwallet.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bogdanmurzin.mypersonalwallet.R
import com.bogdanmurzin.mypersonalwallet.adapter.ImageRecyclerViewAdapter
import com.bogdanmurzin.mypersonalwallet.common.Constants
import com.bogdanmurzin.mypersonalwallet.util.Event
import com.google.android.material.appbar.MaterialToolbar

abstract class CategoryFragment : Fragment() {

    protected lateinit var imageRecyclerAdapter: ImageRecyclerViewAdapter
    protected var toolbar: MaterialToolbar? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        observeViewModel()
        setupRecycler()
    }

    abstract fun observeViewModel()
    abstract fun setupRecycler()
    abstract fun add()

    override fun onDestroy() {
        super.onDestroy()
        updateToolbar(false)
    }

    protected fun updateToolbar(isShowAddIcon: Boolean) {
        toolbar?.let {
            it.menu.findItem(R.id.m_add).isVisible = isShowAddIcon
            it.menu.findItem(R.id.m_delete).isVisible = !isShowAddIcon
            it.menu.findItem(R.id.m_settings).isVisible = !isShowAddIcon
        }
    }
}