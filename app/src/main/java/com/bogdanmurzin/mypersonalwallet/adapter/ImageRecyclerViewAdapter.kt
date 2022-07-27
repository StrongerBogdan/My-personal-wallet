package com.bogdanmurzin.mypersonalwallet.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bogdanmurzin.domain.entities.CategoryEntity
import com.bogdanmurzin.mypersonalwallet.common.Constants.ICON_SCALE
import com.bogdanmurzin.mypersonalwallet.databinding.RvAccountTypeItemBinding
import com.bumptech.glide.Glide

class ImageRecyclerViewAdapter(
    private val onItemClicked: (CategoryEntity) -> Unit
) :
    ListAdapter<CategoryEntity, ImageRecyclerViewAdapter.ViewHolder>(ItemDiffCallback) {

    lateinit var context: Context

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ImageRecyclerViewAdapter.ViewHolder {
        context = parent.context
        return ViewHolder(
            RvAccountTypeItemBinding
                .inflate(LayoutInflater.from(parent.context), parent, false),
            onItemClicked
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class ViewHolder(
        private val binding: RvAccountTypeItemBinding,
        private var onEdit: (CategoryEntity) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(entity: CategoryEntity) {
            binding.titleAccountType.text = entity.title
            Glide.with(context)
                .load(entity.imageUri)
                .override(ICON_SCALE, ICON_SCALE)
                .into(binding.ivIcon)

            binding.root.setOnClickListener {
                onEdit(entity)
            }
        }
    }

    object ItemDiffCallback : DiffUtil.ItemCallback<CategoryEntity>() {
        override fun areItemsTheSame(
            oldItem: CategoryEntity,
            newItem: CategoryEntity
        ): Boolean {
            return oldItem == newItem
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
