package dev.maxsiomin.libpass.fragments.confirm

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.maxsiomin.libpass.R
import dev.maxsiomin.libpass.databinding.FragmentConfirmBinding
import dev.maxsiomin.libpass.fragments.base.BaseFragment

@AndroidEntryPoint
class ConfirmFragment : BaseFragment(R.layout.fragment_confirm) {

    private lateinit var binding: FragmentConfirmBinding

    override val mRoot get() = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentConfirmBinding.bind(view)

        binding.apply {
            val libpassId = requireArguments().getString(ARG_LIBPASS_ID)!!
            idTextview.text = libpassId

            buttonNo.setOnClickListener {
                findNavController().popBackStack()
            }

            buttonYes.setOnClickListener {
                val direction = ConfirmFragmentDirections.actionConfirmFragmentToEnterAliasFragment(libpassId)
                findNavController().navigate(direction)
            }
        }
    }

    companion object {
        private const val ARG_LIBPASS_ID = "libpassId"
    }
}
