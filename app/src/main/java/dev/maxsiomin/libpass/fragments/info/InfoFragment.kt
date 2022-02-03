package dev.maxsiomin.libpass.fragments.info

import android.os.Bundle
import android.view.View
import dagger.hilt.android.AndroidEntryPoint
import dev.maxsiomin.libpass.R
import dev.maxsiomin.libpass.databinding.FragmentInfoBinding
import dev.maxsiomin.libpass.fragments.base.BaseFragment

@AndroidEntryPoint
class InfoFragment : BaseFragment(R.layout.fragment_info) {

    private lateinit var binding: FragmentInfoBinding

    override val mRoot get() = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentInfoBinding.bind(view)
    }
}
