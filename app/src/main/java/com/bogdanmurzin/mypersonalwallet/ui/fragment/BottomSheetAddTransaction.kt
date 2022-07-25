package com.bogdanmurzin.mypersonalwallet.ui.fragment

import android.content.res.Configuration
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import com.bogdanmurzin.mypersonalwallet.databinding.FragmentBottomsheetAddTransactionBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.text.NumberFormat


class BottomSheetAddTransaction : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentBottomsheetAddTransactionBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBottomsheetAddTransactionBinding.inflate(layoutInflater)
        setupEditText()
        return binding.root
    }

    private fun setupEditText() {
        val textview = binding.transactionAmountTv
        textview.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit

            override fun afterTextChanged(s: Editable?) {
                if (s != null) {
                    textview.removeTextChangedListener(this)
                    val cleanString: String = s.replace("""[$,.]""".toRegex(), "")
                    val parsed = cleanString.toDouble()
                    val formatted = NumberFormat.getCurrencyInstance().format((parsed / 100))
                    textview.setText(formatted)
                    textview.setSelection(formatted.length)
                    textview.addTextChangedListener(this)
                }
            }
        })
    }

    companion object {
        fun newInstance() = BottomSheetAddTransaction()
    }
}