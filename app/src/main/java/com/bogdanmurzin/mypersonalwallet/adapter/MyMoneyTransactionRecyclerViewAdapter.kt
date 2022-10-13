package com.bogdanmurzin.mypersonalwallet.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bogdanmurzin.mypersonalwallet.R
import com.bogdanmurzin.mypersonalwallet.common.Constants
import com.bogdanmurzin.mypersonalwallet.data.TrxCategoryUiModel
import com.bogdanmurzin.mypersonalwallet.data.transaction_recycer_items.HeaderItemUiModel
import com.bogdanmurzin.mypersonalwallet.data.transaction_recycer_items.RecyclerMultiTypeItem
import com.bogdanmurzin.mypersonalwallet.data.transaction_recycer_items.TransactionItemUiModel
import com.bogdanmurzin.mypersonalwallet.databinding.RvItemHeaderBinding
import com.bogdanmurzin.mypersonalwallet.databinding.RvItemTransactionBinding
import com.bumptech.glide.Glide

class MyMoneyTransactionRecyclerViewAdapter(
    private val onItemClicked: (TransactionItemUiModel) -> Unit,
    private val showMenuDelete: (Boolean) -> Unit,
    private val getAllSelectedItems: (List<TransactionItemUiModel>) -> Unit
) : ListAdapter<RecyclerMultiTypeItem, RecyclerView.ViewHolder>(ItemDiffCallback) {

    var isEnabledDeleting = false
    private var itemSelectedList = mutableListOf<TransactionItemUiModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            // in future it is a good idea to make it through the delegates or factories
            ITEM_TYPE_HEADER -> {
                HeaderViewHolder(
                    RvItemHeaderBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            else -> {
                MoneyTransactionViewHolder(
                    RvItemTransactionBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                ).also { viewHolder ->
                    // Set on short click listener
                    viewHolder.itemView.setOnClickListener {
                        val position = viewHolder.absoluteAdapterPosition
                        val item = getItem(position) as TransactionItemUiModel
                        if (isEnabledDeleting) {
                            // If this item is already selected
                            if (itemSelectedList.contains(item)) {
                                unselectItem(viewHolder, item)
                            } else {
                                // If this item is not selected
                                selectItem(viewHolder, item)
                            }
                            provideAllSelectedItems()
                        } else {
                            onItemClicked(item)
                        }
                    }
                    // Set on long click listener
                    viewHolder.itemView.setOnLongClickListener {
                        val position = viewHolder.absoluteAdapterPosition
                        val item = getItem(position) as TransactionItemUiModel
                        selectItem(viewHolder, item)
                        provideAllSelectedItems()
                        true
                    }
                }
            }
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            ITEM_TYPE_HEADER ->
                (holder as HeaderViewHolder).bind(getItem(position) as HeaderItemUiModel)
            ITEM_TYPE_TRANSACTION ->
                (holder as MoneyTransactionViewHolder).bind(getItem(position) as TransactionItemUiModel)
                    .also {
                        val item = getItem(position) as TransactionItemUiModel
                        if (item.isSelected) {
                            selectItem(holder, item)
                        }
                    }
        }
    }

    override fun getItemViewType(position: Int): Int = getItem(position).type


    inner class HeaderViewHolder(binding: RvItemHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val dayTv: TextView = binding.dayTv
        private val dayOfTheWeekTv: TextView = binding.dayOfTheWeekTv
        private val monthYearTv: TextView = binding.monthYearTv
        private val transactionAmountTv: TextView = binding.transactionAmountTv

        fun bind(item: HeaderItemUiModel) {
            dayTv.text = item.day
            dayOfTheWeekTv.text = item.dayOfTheWeek
            monthYearTv.text = item.monthYear
            transactionAmountTv.text = item.sumOfTransactions
        }
    }

    inner class MoneyTransactionViewHolder(val binding: RvItemTransactionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: TransactionItemUiModel) {
            with(binding) {
                val context = categoryTv.context

                // set picture to Category
                Glide.with(context)
                    .load(Uri.parse(item.category.imageUri))
                    .override(Constants.ICON_SCALE, Constants.ICON_SCALE)
                    .into(categoryIv)

                // set picture to Account type
                Glide.with(context)
                    .load(Uri.parse(item.accountType.imageUri))
                    .override(Constants.ICON_SCALE, Constants.ICON_SCALE)
                    .into(accountIv)

                categoryTv.text = item.category.compositeTitle
                transactionAmountTv.text = item.transactionAmount

                accountTypeTv.text = item.accountType.title
                descriptionTv.text = item.description
                if (item.description.isNullOrEmpty()) descriptionTv.visibility = ViewGroup.GONE
            }

        }
    }

    private fun selectItem(
        holder: MyMoneyTransactionRecyclerViewAdapter.MoneyTransactionViewHolder,
        item: TransactionItemUiModel
    ) {
        isEnabledDeleting = true
        itemSelectedList.add(item)
        item.isSelected = true
        holder.binding.categoryIv.setImageResource(R.drawable.ic_baseline_check)
        showMenuDelete(true)
    }

    private fun unselectItem(
        holder: MoneyTransactionViewHolder,
        item: TransactionItemUiModel
    ) {
        itemSelectedList.remove(item)
        item.isSelected = false
        // return imageView
        setCategoryImageView(
            holder.binding.categoryIv,
            item.category
        )
        if (itemSelectedList.isEmpty()) {
            showMenuDelete(false)
            isEnabledDeleting = false
        }
    }

    private fun provideAllSelectedItems() {
        // Clear item selected list. There may be excess after removal
        itemSelectedList =
            itemSelectedList.filter { currentList.contains(it) } as MutableList<TransactionItemUiModel>
        // Provide data
        getAllSelectedItems(itemSelectedList)
    }

    private fun setCategoryImageView(categoryIv: ImageView, itemCategory: TrxCategoryUiModel) {
        Glide.with(categoryIv.context)
            .load(Uri.parse(itemCategory.imageUri))
            .override(Constants.ICON_SCALE, Constants.ICON_SCALE)
            .into(categoryIv)
    }

    object ItemDiffCallback : DiffUtil.ItemCallback<RecyclerMultiTypeItem>() {
        override fun areItemsTheSame(
            oldItem: RecyclerMultiTypeItem,
            newItem: RecyclerMultiTypeItem
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: RecyclerMultiTypeItem,
            newItem: RecyclerMultiTypeItem
        ): Boolean {
            return oldItem.type == newItem.type
        }
    }

    companion object {
        const val ITEM_TYPE_HEADER = 1
        const val ITEM_TYPE_TRANSACTION = 2
    }
}