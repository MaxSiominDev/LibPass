package dev.maxsiomin.libpass.fragments.entermanually

import android.os.Bundle
import android.view.View
import androidx.core.text.isDigitsOnly
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.zxing.BarcodeFormat
import com.google.zxing.FormatException
import com.google.zxing.MultiFormatWriter
import com.google.zxing.oned.UPCAReader
import dagger.hilt.android.AndroidEntryPoint
import dev.maxsiomin.libpass.R
import dev.maxsiomin.libpass.databinding.FragmentEnterManuallyBinding
import dev.maxsiomin.libpass.extensions.clearError
import dev.maxsiomin.libpass.fragments.base.BaseFragment
import dev.maxsiomin.libpass.fragments.base.BaseViewModel
import java.lang.IllegalArgumentException

/**
 * User can go to this fragment from AddFragment.
 * User is to enter their libpass id here.
 */
@AndroidEntryPoint
class EnterManuallyFragment : BaseFragment(R.layout.fragment_enter_manually) {

    private lateinit var binding: FragmentEnterManuallyBinding

    override val mRoot get() = binding.root

    private val viewModel by viewModels<BaseViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentEnterManuallyBinding.bind(view)

        binding.apply {
            buttonNext.setOnClickListener {
                val currId = libpassEditText.text.toString()
                if (currId.isDigitsOnly() && currId.length == 13 && checksumIsOK(currId))
                    goToConfirmFragment(currId)
                else
                    libpassEditTextLayout.error = viewModel.getString(R.string.wrong_id)
            }

            libpassEditText.addTextChangedListener {
                libpassEditTextLayout.clearError()
            }
        }
    }

    private fun checksumIsOK(id: String): Boolean {
        return try {
            // In case checksum is incorrect returns false
            MultiFormatWriter().encode(id, BarcodeFormat.EAN_13, 0, 0)
            true
        } catch (e: IllegalArgumentException) {
            false
        }
    }

    private fun goToConfirmFragment(id: String) {
        val direction = EnterManuallyFragmentDirections.actionEnterManuallyFragmentToConfirmFragment(id)
        findNavController().navigate(direction)
    }
}
