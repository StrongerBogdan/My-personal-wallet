package com.bogdanmurzin.mypersonalwallet.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bogdanmurzin.domain.entities.CategoryEntity
import com.bogdanmurzin.mypersonalwallet.R
import com.bogdanmurzin.mypersonalwallet.common.Constants.ICON_SCALE
import com.bogdanmurzin.mypersonalwallet.databinding.RvCategoryItemBinding
import com.bumptech.glide.Glide


class ImageRecyclerViewAdapter(
    private val onItemClicked: (CategoryEntity) -> Unit
) :
    ListAdapter<CategoryEntity, ImageRecyclerViewAdapter.ViewHolder>(ItemDiffCallback) {

    lateinit var context: Context
    var selectedPosition = RecyclerView.NO_POSITION

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ImageRecyclerViewAdapter.ViewHolder {
        context = parent.context
        return ViewHolder(
            RvCategoryItemBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
        holder.itemView.setBackgroundResource(if (selectedPosition == position) R.drawable.round_rect_shape_recycler else Color.TRANSPARENT)
        // (+) set clickListener while create holder
        holder.itemView.setOnClickListener {
            // (+) Do you really need two calls of "notifyItemChanged"?
            // First notifyItemChanged for unhighlighting item
            // Second for highlighting selected item
            notifyItemChanged(selectedPosition)
            selectedPosition = position
            notifyItemChanged(selectedPosition)
            onItemClicked(item)
        }
    }

    inner class ViewHolder(
        private val binding: RvCategoryItemBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(entity: CategoryEntity) {
            binding.titleAccountType.text = entity.title
            Glide.with(context)
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
            // (+) here better to compare using type of class and some id
            return oldItem::class == newItem::class && oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: CategoryEntity,
            newItem: CategoryEntity
        ): Boolean {
            // (+- cannot equals interfaces) here in most of cases used equals
            return oldItem.title == newItem.title &&
                    oldItem.imageUri == newItem.imageUri
        }
    }
}
