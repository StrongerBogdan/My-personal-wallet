package com.bogdanmurzin.mypersonalwallet.ui.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.Button
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.bogdanmurzin.mypersonalwallet.R
import com.bogdanmurzin.mypersonalwallet.common.Constants
import com.bogdanmurzin.mypersonalwallet.databinding.DialogFragmentColorPickerBinding
import com.bogdanmurzin.mypersonalwallet.util.setNavigationResult

class ChooseColorFragmentDialog : DialogFragment() {

    private lateinit var binding: DialogFragmentColorPickerBinding
    private lateinit var colorBtn1: Button
    private lateinit var colorBtn2: Button
    private lateinit var colorBtn3: Button
    private lateinit var colorBtn4: Button

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogFragmentColorPickerBinding.inflate(layoutInflater)
        colorBtn1 = binding.colorPick1
        colorBtn2 = binding.colorPick2
        colorBtn3 = binding.colorPick3
        colorBtn4 = binding.colorPick4

        setupListeners()

        val dialogBuilder = AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.choose_color))
            .setNegativeButton(getString(R.string.cancel)) { _, _ ->
                dismiss()
            }
            .setView(binding.root)

        return dialogBuilder.create()
    }

    private fun setupListeners() {
        colorBtn1.setOnClickListener {
            returnThemeId(R.style.OverlayPrimaryColorGreen)
        }
        colorBtn2.setOnClickListener {
            returnThemeId(R.style.OverlayPrimaryColorPurple)
        }
        colorBtn3.setOnClickListener {
            returnThemeId(R.style.OverlayPrimaryColorCoral)
        }
        colorBtn4.setOnClickListener {
            returnThemeId(R.style.OverlayPrimaryColorOrange)
        }
    }

    private fun returnThemeId(themeId: Int) {
        setNavigationResult(themeId, Constants.COLOR_RESULT)
        findNavController().navigateUp()
    }
}
