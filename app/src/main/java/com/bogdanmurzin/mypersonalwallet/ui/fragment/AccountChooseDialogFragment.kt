package com.bogdanmurzin.mypersonalwallet.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bogdanmurzin.domain.entities.AccountType
import com.bogdanmurzin.domain.entities.EntityWithImageAndTitle
import com.bogdanmurzin.mypersonalwallet.adapter.ImageRecyclerViewAdapter
import com.bogdanmurzin.mypersonalwallet.databinding.DialogAccountChooseBinding

class AccountChooseDialogFragment : DialogFragment() {

    private lateinit var binding: DialogAccountChooseBinding
    private lateinit var recyclerAdapter: ImageRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogAccountChooseBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecycler()
        recyclerAdapter.submitList(testIconList())
    }

    private fun setupRecycler() {
        val layoutManager: RecyclerView.LayoutManager =
            GridLayoutManager(requireContext(), SPAN_COUNT)
        recyclerAdapter = ImageRecyclerViewAdapter {
            // implement return Account type
            Toast.makeText(
                requireContext(),
                "You taped on ${it.title} account",
                Toast.LENGTH_SHORT
            ).show()
        }
        val recyclerView = binding.accountRecycler
        recyclerView.adapter = recyclerAdapter
        recyclerView.layoutManager = layoutManager
    }

    fun testIconList(): List<EntityWithImageAndTitle> =
        listOf(
            AccountType("title1", "https://cdn4.iconfinder.com/data/icons/48-bubbles/48/07.Wallet-48.png"),
            AccountType("title2", "https://cdn4.iconfinder.com/data/icons/48-bubbles/48/07.Wallet-48.png"),
            AccountType("title3", "https://cdn4.iconfinder.com/data/icons/48-bubbles/48/07.Wallet-48.png"),
            AccountType("title4", "https://cdn4.iconfinder.com/data/icons/48-bubbles/48/07.Wallet-48.png"),
            AccountType("title5", "https://cdn4.iconfinder.com/data/icons/48-bubbles/48/07.Wallet-48.png"),
            AccountType("title6", "https://cdn4.iconfinder.com/data/icons/48-bubbles/48/07.Wallet-48.png"),
            AccountType("title7", "https://cdn4.iconfinder.com/data/icons/48-bubbles/48/07.Wallet-48.png"),
            AccountType("title8", "https://cdn4.iconfinder.com/data/icons/48-bubbles/48/07.Wallet-48.png"),
            AccountType("title9", "https://cdn4.iconfinder.com/data/icons/48-bubbles/48/07.Wallet-48.png"),
            AccountType("title10", "https://cdn4.iconfinder.com/data/icons/48-bubbles/48/07.Wallet-48.png"),
            AccountType("title11", "https://cdn4.iconfinder.com/data/icons/48-bubbles/48/07.Wallet-48.png"),
            AccountType("title12", "https://cdn4.iconfinder.com/data/icons/48-bubbles/48/07.Wallet-48.png"),
            AccountType("title13", "https://cdn4.iconfinder.com/data/icons/48-bubbles/48/07.Wallet-48.png"),
            AccountType("title14", "https://cdn4.iconfinder.com/data/icons/48-bubbles/48/07.Wallet-48.png"),
            AccountType("title15", "https://cdn4.iconfinder.com/data/icons/48-bubbles/48/07.Wallet-48.png"),
            AccountType("title16", "https://cdn4.iconfinder.com/data/icons/48-bubbles/48/07.Wallet-48.png"),
            AccountType("title17", "https://cdn4.iconfinder.com/data/icons/48-bubbles/48/07.Wallet-48.png"),
            AccountType("title18", "https://cdn4.iconfinder.com/data/icons/48-bubbles/48/07.Wallet-48.png"),
            AccountType("title19", "https://cdn4.iconfinder.com/data/icons/48-bubbles/48/07.Wallet-48.png"),
            AccountType("title20", "https://cdn4.iconfinder.com/data/icons/48-bubbles/48/07.Wallet-48.png"),
            AccountType("title21", "https://cdn4.iconfinder.com/data/icons/aiga-symbol-signs/388/aiga_taxi-48.png"),
            AccountType("title22", "https://cdn4.iconfinder.com/data/icons/aiga-symbol-signs/388/aiga_taxi-48.png"),
            AccountType("title23", "https://cdn4.iconfinder.com/data/icons/aiga-symbol-signs/388/aiga_taxi-48.png"),
            AccountType("title24", "https://cdn4.iconfinder.com/data/icons/aiga-symbol-signs/388/aiga_taxi-48.png"),
            AccountType("title25", "https://cdn4.iconfinder.com/data/icons/aiga-symbol-signs/388/aiga_taxi-48.png"),
            AccountType("title26", "https://cdn4.iconfinder.com/data/icons/aiga-symbol-signs/388/aiga_taxi-48.png"),
            AccountType("title27", "https://cdn4.iconfinder.com/data/icons/aiga-symbol-signs/388/aiga_taxi-48.png"),
            AccountType("title28", "https://cdn4.iconfinder.com/data/icons/aiga-symbol-signs/388/aiga_taxi-48.png"),
            AccountType("title29", "https://cdn4.iconfinder.com/data/icons/aiga-symbol-signs/388/aiga_taxi-48.png"),
            AccountType("title30", "https://cdn4.iconfinder.com/data/icons/aiga-symbol-signs/388/aiga_taxi-48.png")
        )

    companion object {
        const val SPAN_COUNT = 4
    }
}