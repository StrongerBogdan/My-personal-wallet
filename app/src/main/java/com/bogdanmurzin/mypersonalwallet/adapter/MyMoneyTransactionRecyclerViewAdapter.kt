package com.bogdanmurzin.mypersonalwallet.adapter

import android.graphics.drawable.BitmapDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bogdanmurzin.mypersonalwallet.R
import com.bogdanmurzin.mypersonalwallet.data.transaction_recycer_items.HeaderItemUiModel
import com.bogdanmurzin.mypersonalwallet.data.transaction_recycer_items.TransactionItemUiModel
import com.bogdanmurzin.mypersonalwallet.databinding.RvItemHeaderBinding
import com.bogdanmurzin.mypersonalwallet.databinding.RvItemTransactionBinding
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class MyMoneyTransactionRecyclerViewAdapter(
    private val onItemClicked: (TransactionItemUiModel) -> Unit
) : ListAdapter<TransactionItemUiModel, RecyclerView.ViewHolder>(ItemDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
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
            val locate = Locale.getDefault()

            dayTv.text =
                SimpleDateFormat("dd", locate).format(item.date)
            dayOfTheWeekTv.text =
                SimpleDateFormat("EEE", locate).format(item.date)
                    .uppercase()
            monthYearTv.text =
                SimpleDateFormat("MMMM yyyy", locate).format(item.date)
                    .uppercase()
            transactionAmountTv.text =
                NumberFormat.getCurrencyInstance(locate).format(item.transactionAmount)
        }
    }

    inner class MoneyTransactionViewHolder(binding: RvItemTransactionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val imageView: ImageView = binding.categoryIv
        private val categoryTv: TextView = binding.categoryTv
        private val accountTypeTv: TextView = binding.accountTypeTv
        private val descriptionTv: TextView = binding.descriptionTv
        private val transactionAmountTv: TextView = binding.transactionAmountTv

        fun bind(item: TransactionItemUiModel) {
            val locate = Locale.getDefault()
            val context = categoryTv.context

            // get picture TODO refactor
            val transactionPic = if (item.category.transactionPicUri.isNullOrBlank()) {
                // Default resource icon
                ContextCompat.getDrawable(context, R.drawable.ic_card)
            } else {
                BitmapDrawable(context.resources, item.category.transactionPicUri)
            }
            imageView.setImageDrawable(transactionPic)
            //imageView.setImageDrawable()
            categoryTv.text =
                if (item.category.subcategory != null) {
                    context.getString(
                        R.string.category_with_subcategory,
                        item.category.title,
                        item.category.subcategory
                    )
                } else {
                    item.category.title
                }
            accountTypeTv.text = item.accountType.title

            // get picture
            val accountPic =
                if (item.accountType.accountPicUri.isNullOrBlank()) {
                    // Default resource icon
                    ContextCompat.getDrawable(context, R.drawable.ic_shopping_cart)
                } else {
                    BitmapDrawable(context.resources, item.accountType.accountPicUri)
                }
            accountTypeTv.setCompoundDrawablesWithIntrinsicBounds(
                accountPic,
                null,
                null,
                null
            )
            descriptionTv.text = item.description
            transactionAmountTv.text =
                NumberFormat.getCurrencyInstance(locate).format(item.transactionAmount)
        }
    }

    object ItemDiffCallback : DiffUtil.ItemCallback<TransactionItemUiModel>() {
        override fun areItemsTheSame(
            oldItem: TransactionItemUiModel,
            newItem: TransactionItemUiModel
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: TransactionItemUiModel,
            newItem: TransactionItemUiModel
        ): Boolean {
            return oldItem.accountType == newItem.accountType &&
                    oldItem.category == newItem.category &&
                    oldItem.description == newItem.description
        }
    }

    companion object {
        const val ITEM_TYPE_HEADER = 1
        const val ITEM_TYPE_TRANSACTION = 2
    }
}