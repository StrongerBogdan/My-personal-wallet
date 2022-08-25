package com.bogdanmurzin.mypersonalwallet.adapter

import android.graphics.Color
import com.bogdanmurzin.domain.entities.CategoryEntity
import com.bogdanmurzin.mypersonalwallet.R

class ImageRecyclerViewAdapterWithSelection(
    private val onItemClicked: (CategoryEntity) -> Unit
) : ImageRecyclerViewAdapter(onItemClicked) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
        holder.itemView.setBackgroundResource(if (selectedPosition == position) R.drawable.round_rect_shape_recycler else Color.TRANSPARENT)
        holder.itemView.setOnClickListener {
            // First notifyItemChanged for unhighlighting item
            // Second for highlighting selected item
            notifyItemChanged(selectedPosition)
            selectedPosition = position
            notifyItemChanged(selectedPosition)
            onItemClicked(item)
        }
    }

}
