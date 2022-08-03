package com.bogdanmurzin.mypersonalwallet.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bogdanmurzin.mypersonalwallet.R
import com.bogdanmurzin.mypersonalwallet.common.Constants
import com.bogdanmurzin.mypersonalwallet.data.transaction_recycer_items.HeaderItemUiModel
import com.bogdanmurzin.mypersonalwallet.data.transaction_recycer_items.TransactionItemUiModel
import com.bogdanmurzin.mypersonalwallet.databinding.RvItemHeaderBinding
import com.bogdanmurzin.mypersonalwallet.databinding.RvItemTransactionBinding
import com.bumptech.glide.Glide
import java.text.NumberFormat
import java.util.*

class MyMoneyTransactionRecyclerViewAdapter(
    private val onItemClicked: (TransactionItemUiModel) -> Unit
) : ListAdapter<TransactionItemUiModel, RecyclerView.ViewHolder>(ItemDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            // TODO in future it is a good idea to make it through the delegates or factories
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
                    viewHolder.itemView.setOnClickListener {
                        onItemClicked(getItem(viewHolder.absoluteAdapterPosition))
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
            // (+) these formatting needs to do in mapper in viewmodel layer
            // bind method should be used only for setting value to fields of the holder or make UI specific actions
            dayTv.text = item.day
            dayOfTheWeekTv.text = item.dayOfTheWeek
            monthYearTv.text = item.monthYear
            transactionAmountTv.text = item.sumOfTransactions
        }
    }

    inner class MoneyTransactionViewHolder(private val binding: RvItemTransactionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        // (+) you can simply use binding, local properties is not necessary

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

                // (+) the same as for HeaderViewHolder
                categoryTv.text = item.category.title
                transactionAmountTv.text = item.transactionAmount

                accountTypeTv.text = item.accountType.title
                descriptionTv.text = item.description
                if (item.description.isNullOrEmpty()) descriptionTv.visibility = ViewGroup.GONE
            }

        }
    }

    // (+) the same as for ImageRecyclerViewAdapter
    object ItemDiffCallback : DiffUtil.ItemCallback<TransactionItemUiModel>() {
        override fun areItemsTheSame(
            oldItem: TransactionItemUiModel,
            newItem: TransactionItemUiModel
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: TransactionItemUiModel,
            newItem: TransactionItemUiModel
        ): Boolean {
            return oldItem == newItem
        }
    }

    companion object {
        const val ITEM_TYPE_HEADER = 1
        const val ITEM_TYPE_TRANSACTION = 2
    }
}