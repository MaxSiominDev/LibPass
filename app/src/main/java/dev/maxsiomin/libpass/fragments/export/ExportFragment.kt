package dev.maxsiomin.libpass.fragments.export

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.journeyapps.barcodescanner.BarcodeEncoder
import dagger.hilt.android.AndroidEntryPoint
import dev.maxsiomin.libpass.R
import dev.maxsiomin.libpass.database.LibraryPass
import dev.maxsiomin.libpass.databinding.FragmentExportBinding
import dev.maxsiomin.libpass.extensions.runOnUiThread
import dev.maxsiomin.libpass.fragments.base.BaseFragment
import dev.maxsiomin.libpass.fragments.base.BaseViewModel
import dev.maxsiomin.libpass.util.runOnIoCoroutine

@AndroidEntryPoint
class ExportFragment : BaseFragment(R.layout.fragment_export) {

    private lateinit var binding: FragmentExportBinding

    override val mRoot get() = binding.root

    private val viewModel by viewModels<BaseViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentExportBinding.bind(view)

        // Create instance in the main thread
        viewModel

        runOnIoCoroutine {
            val libpasses = viewModel.dao.loadAllPasses()

            if (libpasses.isNullOrEmpty()) {
                runOnUiThread {
                    viewModel.toast(R.string.no_libpasses, Toast.LENGTH_SHORT)
                    findNavController().popBackStack()
                }
                return@runOnIoCoroutine
            }

            runOnUiThread { setupQrCode(libpasses) }
        }
    }

    private fun setupQrCode(libpasses: List<LibraryPass>) {
        var text = ""
        libpasses.forEach {
            if (text != "")
                text += "||"

            text += it.value
            text += "|"
            text += it.alias
        }

        val size = viewModel.getDimen(R.dimen.qr_size)
        val matrix = MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, size, size)
        binding.qrImage.setImageBitmap(BarcodeEncoder().createBitmap(matrix))
    }
}
