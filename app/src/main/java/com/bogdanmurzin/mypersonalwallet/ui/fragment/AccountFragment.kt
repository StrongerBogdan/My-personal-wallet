package com.bogdanmurzin.mypersonalwallet.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bogdanmurzin.mypersonalwallet.databinding.FragmentAccountBinding
import com.bogdanmurzin.mypersonalwallet.ui.activity.IconsActivity

class AccountFragment : Fragment() {

    private lateinit var binding: FragmentAccountBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAccountBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.textBtn.setOnClickListener {
            val intent = Intent(requireContext(), IconsActivity::class.java)
            startActivity(intent)
        }
    }

}