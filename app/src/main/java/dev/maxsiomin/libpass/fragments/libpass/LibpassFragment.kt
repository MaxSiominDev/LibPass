package dev.maxsiomin.libpass.fragments.libpass

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.journeyapps.barcodescanner.BarcodeEncoder
import dagger.hilt.android.AndroidEntryPoint
import dev.maxsiomin.libpass.R
import dev.maxsiomin.libpass.databinding.FragmentLibpassBinding
import dev.maxsiomin.libpass.extensions.runOnUiThread
import dev.maxsiomin.libpass.fragments.base.BaseFragment

@AndroidEntryPoint
class LibpassFragment : BaseFragment(R.layout.fragment_libpass) {

    private lateinit var binding: FragmentLibpassBinding

    override val mRoot get() = binding.root

    private val viewModel by viewModels<LibpassViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentLibpassBinding.bind(view)

        viewModel.checkForLibpass(this::onLibpassLoaded, this::goToAddFragment)

        binding.apply {
            buttonNewLibpass.setOnClickListener {
                findNavController().navigate(R.id.action_libpassFragment_to_addFragment)
            }
        }
    }

    private fun onLibpassLoaded(value: String) {
        runOnUiThread {
            binding.idTextView.text = value
            setupBarcode(value)
        }
    }

    private fun goToAddFragment() {
        runOnUiThread {
            findNavController().navigate(R.id.action_libpassFragment_to_addFragment)
        }
    }

    private fun setupBarcode(text: String) {
        val width = viewModel.getDimen(R.dimen.barcode_width)
        val height = viewModel.getDimen(R.dimen.barcode_height)
        val matrix = MultiFormatWriter().encode(text, BarcodeFormat.EAN_13, width, height)
        binding.barcodeImage.setImageBitmap(BarcodeEncoder().createBitmap(matrix))
    }
}
