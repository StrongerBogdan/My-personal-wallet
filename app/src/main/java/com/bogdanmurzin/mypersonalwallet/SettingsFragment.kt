package com.bogdanmurzin.mypersonalwallet

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bogdanmurzin.mypersonalwallet.common.Constants
import com.bogdanmurzin.mypersonalwallet.databinding.FragmentSettingsBinding
import com.bogdanmurzin.mypersonalwallet.ui.viewmodel.SettingsViewModel
import com.bogdanmurzin.mypersonalwallet.util.Event
import com.bogdanmurzin.mypersonalwallet.util.getNavigationResultLiveData
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    private val viewModel: SettingsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(layoutInflater)

        with(activity as AppCompatActivity) {
            setSupportActionBar(binding.toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowHomeEnabled(true)
        }

        binding.settingsThemeColor.setOnClickListener {
            viewModel.openThemeColorChooser()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.action.observe(viewLifecycleOwner) { event ->
            if (event == Event.OpenThemeColorChooser) {
                findNavController().navigate(
                    SettingsFragmentDirections
                        .actionSettingsFragmentToSettingsFragmentDialog()
                )
            }
        }

        viewModel.themeColor.observe(viewLifecycleOwner) { themeResId ->
            // get color and set the theme
            val theme = requireContext().theme
            theme.applyStyle(themeResId, true)
            val typedValue = TypedValue()
            theme.resolveAttribute(android.R.attr.colorPrimary, typedValue, true)
            @ColorInt val color = typedValue.data

            // changeButtonColor(color)
            binding.settingsThemeColor.background.setTint(color)
            binding.toolbar.background.setTint(color)
        }

        // get result from Choosing color
        val themeColorResult =
            getNavigationResultLiveData<Int>(Constants.COLOR_RESULT)
        themeColorResult?.observe(viewLifecycleOwner) { themeId ->
            viewModel.changeThemeColor(themeId)
        }

    }

    private fun changeButtonColor(color: Int) {
        val unwrappedDrawable =
            AppCompatResources.getDrawable(requireContext(), R.drawable.round_rect_shape_recycler)
        if (unwrappedDrawable != null) {
            val wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable)
            DrawableCompat.setTint(wrappedDrawable, color)

            binding.settingsThemeColor.background = wrappedDrawable
        }


    }

}