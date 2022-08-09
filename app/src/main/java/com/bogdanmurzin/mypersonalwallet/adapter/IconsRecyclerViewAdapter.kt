package com.bogdanmurzin.mypersonalwallet.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bogdanmurzin.domain.entities.Icon
import com.bogdanmurzin.mypersonalwallet.databinding.RecyclerItemImageBinding
import com.bumptech.glide.Glide


class IconsRecyclerViewAdapter(val onIconClicked: (Icon) -> Unit) :
    ListAdapter<Icon, IconsRecyclerViewAdapter.ViewHolder>(ItemDiffCallback) {

    lateinit var context: Context

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): IconsRecyclerViewAdapter.ViewHolder {
        context = parent.context
        return ViewHolder(
            RecyclerItemImageBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
        holder.itemView.setOnClickListener {
            onIconClicked(item)
        }
    }

    inner class ViewHolder(private val binding: RecyclerItemImageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(icon: Icon) {
            Glide.with(context)
                .load(icon.preview)
                .override(ICON_SCALE, ICON_SCALE)
                .into(binding.ivIcon)
        }
    }

    object ItemDiffCallback : DiffUtil.ItemCallback<Icon>() {
        override fun areItemsTheSame(oldItem: Icon, newItem: Icon): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: Icon,
            newItem: Icon
        ): Boolean {
            return oldItem.iconId == newItem.iconId &&
                    oldItem.preview == newItem.preview &&
                    oldItem.isPaid == newItem.isPaid &&
                    oldItem.download == newItem.download
        }
    }

    companion object {
        const val ICON_SCALE = 90
    }
}