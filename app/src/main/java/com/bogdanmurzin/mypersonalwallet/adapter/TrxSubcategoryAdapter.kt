package com.bogdanmurzin.mypersonalwallet.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bogdanmurzin.domain.entities.TransactionCategory
import com.bogdanmurzin.mypersonalwallet.databinding.RvSubcategoryItemBinding
import com.google.android.material.chip.ChipGroup


class TrxSubcategoryAdapter(
    private val onItemClicked: (TransactionCategory) -> Unit
) :
    ListAdapter<TransactionCategory, TrxSubcategoryAdapter.ViewHolder>(ItemDiffCallback) {

    lateinit var context: Context
    private var selectedItem = RecyclerView.NO_POSITION
    //var chipGroup = ChipGroup(parentView.getContext())

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TrxSubcategoryAdapter.ViewHolder {
        context = parent.context
        return ViewHolder(
            RvSubcategoryItemBinding
                .inflate(LayoutInflater.from(parent.context), parent, false),
            onItemClicked
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
//        holder.itemView.isSelected = selectedItem == position
        holder.bind(item)


//        holder.itemView.setOnClickListener {
//            Log.i(TAG, "onBindViewHolder: ")
//            notifyItemChanged(selectedItem)
//            selectedItem = holder.absoluteAdapterPosition
//            notifyItemChanged(selectedItem)
//        }
    }

    inner class ViewHolder(
        private val binding: RvSubcategoryItemBinding,
        private var onSelectSubcategory: (TransactionCategory) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(entity: TransactionCategory) {
            binding.subcategoryChip.text = entity.subcategory

            //binding.subcategoryChip.check(if (selectedItem == position) Color.GREEN else Color.TRANSPARENT)

            binding.root.setOnClickListener {
                onSelectSubcategory(entity)
//                if (absoluteAdapterPosition == RecyclerView.NO_POSITION) return@setOnClickListener
//
//                notifyItemChanged(selectedItem);
//                selectedItem = absoluteAdapterPosition
//                notifyItemChanged(selectedItem);
            }
        }
    }

    object ItemDiffCallback : DiffUtil.ItemCallback<TransactionCategory>() {
        override fun areItemsTheSame(
            oldItem: TransactionCategory,
            newItem: TransactionCategory
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: TransactionCategory,
            newItem: TransactionCategory
        ): Boolean {
            return oldItem.title == newItem.title &&
                    oldItem.imageUri == newItem.imageUri
        }
    }
}