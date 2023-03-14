package com.bogdanmurzin.mypersonalwallet.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bogdanmurzin.domain.entities.CategoryEntity
import com.bogdanmurzin.mypersonalwallet.common.Constants.ICON_SCALE
import com.bogdanmurzin.mypersonalwallet.databinding.RvCategoryItemBinding
import com.bumptech.glide.Glide

open class ImageRecyclerViewAdapter(
    private val onItemClicked: (CategoryEntity) -> Unit
) :
    ListAdapter<CategoryEntity, ImageRecyclerViewAdapter.ViewHolder>(ItemDiffCallback) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ImageRecyclerViewAdapter.ViewHolder {
        parent.context
        return ViewHolder(
            RvCategoryItemBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
        holder.itemView.setOnClickListener { onItemClicked(item) }
    }

    inner class ViewHolder(
        private val binding: RvCategoryItemBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(entity: CategoryEntity) {
            binding.titleAccountType.text = entity.title
            Glide.with(binding.root.context)
                .load(entity.imageUri)
                .override(ICON_SCALE, ICON_SCALE)
                .into(binding.ivIcon)
        }
    }

    object ItemDiffCallback : DiffUtil.ItemCallback<CategoryEntity>() {
        override fun areItemsTheSame(
            oldItem: CategoryEntity,
            newItem: CategoryEntity
        ): Boolean {
            return oldItem::class == newItem::class && oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: CategoryEntity,
            newItem: CategoryEntity
        ): Boolean {
            return oldItem.title == newItem.title &&
                    oldItem.imageUri == newItem.imageUri
        }
    }
}
