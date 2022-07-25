package com.bogdanmurzin.mypersonalwallet.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bogdanmurzin.mypersonalwallet.databinding.FragmentBottomsheetAddTransactionBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class BottomSheetAddEditTransaction : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentBottomsheetAddTransactionBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBottomsheetAddTransactionBinding.inflate(layoutInflater)
        return binding.root
    }

    companion object {
        // if add new transaction - id equals 0, if edit transaction - legit id
        fun newInstance(id: Int = 0) = BottomSheetAddEditTransaction().apply {
            arguments = Bundle().apply {
                putInt(TRANSACTION_ID_KEY, id)
            }
        }

        private const val TRANSACTION_ID_KEY = "id_key"
    }
}