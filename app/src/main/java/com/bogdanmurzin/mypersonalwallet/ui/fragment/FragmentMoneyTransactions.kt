package com.bogdanmurzin.mypersonalwallet.ui.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bogdanmurzin.domain.entities.AccountType
import com.bogdanmurzin.domain.entities.MoneyTransactionCategory
import com.bogdanmurzin.mypersonalwallet.R
import com.bogdanmurzin.mypersonalwallet.adapter.MyMoneyTransactionRecyclerViewAdapter
import com.bogdanmurzin.mypersonalwallet.data.transaction_recycer_items.HeaderItem
import com.bogdanmurzin.mypersonalwallet.data.transaction_recycer_items.RecyclerMultiTypeItem
import com.bogdanmurzin.mypersonalwallet.data.transaction_recycer_items.TransactionItem
import java.math.BigDecimal
import java.util.*

class FragmentMoneyTransactions : Fragment() {

    private lateinit var thisContext: Context

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_money_transactions_list, container, false)
        if (container != null) {
            thisContext = container.context
        }

        // Set the adapter
        if (view is RecyclerView) {
            view.adapter = MyMoneyTransactionRecyclerViewAdapter(setDataSample())
        }
        return view
    }

    private fun setDataSample(): List<RecyclerMultiTypeItem> =
        listOf(
            HeaderItem(Date(1651680349000), BigDecimal(123)),
            TransactionItem(
                MoneyTransactionCategory(
                    "Fun",
                    "Cinema",
                    null
                    //ContextCompat.getDrawable(thisContext, R.drawable.ic_shopping_cart)
                ),
                null,
                AccountType("card", null), //ContextCompat.getDrawable(thisContext, R.drawable.ic_card)
                BigDecimal(1234)
            ),
            TransactionItem(
                MoneyTransactionCategory(
                    "Grocery",
                    null,
                    null
                    //ContextCompat.getDrawable(thisContext, R.drawable.ic_shopping_cart)
                ),
                null,
                AccountType(
                    "credit card",
                    null
                    //ContextCompat.getDrawable(thisContext, R.drawable.ic_card)
                ),
                BigDecimal(12442)
            ),
            HeaderItem(Date(1651507549), BigDecimal(20233)),
            TransactionItem(
                MoneyTransactionCategory(
                    "Transport",
                    "Taxi",
                    null
                    //ContextCompat.getDrawable(thisContext, R.drawable.ic_card)
                ),
                null,
                AccountType(
                    "wallet",
                    null
                    //ContextCompat.getDrawable(thisContext, R.drawable.ic_shopping_cart)
                ),
                BigDecimal(111)
            ),
            TransactionItem(
                MoneyTransactionCategory(
                    "Home",
                    null,
                    null
                    //ContextCompat.getDrawable(thisContext, R.drawable.ic_shopping_cart)
                ),
                "Huh ^_^",
                AccountType(
                    "credit card",
                    null
                    //ContextCompat.getDrawable(thisContext, R.drawable.ic_card)
                ),
                BigDecimal(12)
            )
        )
}