package com.bogdanmurzin.mypersonalwallet.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bogdanmurzin.domain.entities.EntityWithImageAndTitle
import com.bogdanmurzin.mypersonalwallet.databinding.RvAccountTypeItemBinding
import com.bumptech.glide.Glide

class ImageRecyclerViewAdapter :
    ListAdapter<EntityWithImageAndTitle, ImageRecyclerViewAdapter.ViewHolder>(ItemDiffCallback) {

    lateinit var context: Context

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ImageRecyclerViewAdapter.ViewHolder {
        context = parent.context
        return ViewHolder(
            RvAccountTypeItemBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class ViewHolder(private val binding: RvAccountTypeItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(entity: EntityWithImageAndTitle) {
            binding.titleAccountType.text = entity.title
            Glide.with(context)
                .load(entity.imageUri)
                .override(ICON_SCALE, ICON_SCALE)
                .into(binding.ivIcon)
        }
    }

    object ItemDiffCallback : DiffUtil.ItemCallback<EntityWithImageAndTitle>() {
        override fun areItemsTheSame(
            oldItem: EntityWithImageAndTitle,
            newItem: EntityWithImageAndTitle
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: EntityWithImageAndTitle,
            newItem: EntityWithImageAndTitle
        ): Boolean {
            return oldItem.title == newItem.title &&
                    oldItem.imageUri == newItem.imageUri
        }
    }

    companion object {
        const val ICON_SCALE = 90
    }
}
