package com.bogdanmurzin.mypersonalwallet.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.bogdanmurzin.mypersonalwallet.R
import com.bogdanmurzin.mypersonalwallet.databinding.FragmentAddSubcategoryBinding
import com.bogdanmurzin.mypersonalwallet.ui.viewmodel.AddTrxCategoryViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddSubcategoryFragment : DialogFragment() {

    private lateinit var binding: FragmentAddSubcategoryBinding
    private val viewModel: AddTrxCategoryViewModel by navGraphViewModels(R.id.add_trx_category_flow_graph) {
        defaultViewModelProviderFactory
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddSubcategoryBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.doneBtn.setOnClickListener {
            if (viewModel.addNewSubcategory(binding.subcategoryTitle.text.toString())) {
                findNavController().navigateUp()
            }
        }
    }
}