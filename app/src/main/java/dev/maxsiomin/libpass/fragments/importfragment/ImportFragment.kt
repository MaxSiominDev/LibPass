package dev.maxsiomin.libpass.fragments.importfragment

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.github.dhaval2404.imagepicker.ImagePicker
import dagger.hilt.android.AndroidEntryPoint
import dev.maxsiomin.libpass.R
import dev.maxsiomin.libpass.database.LibraryPass
import dev.maxsiomin.libpass.databinding.FragmentImportBinding
import dev.maxsiomin.libpass.extensions.runOnUiThread
import dev.maxsiomin.libpass.fragments.base.BaseFragment

@AndroidEntryPoint
class ImportFragment : BaseFragment(R.layout.fragment_import) {

    private lateinit var binding: FragmentImportBinding

    override val mRoot get() = binding.root

    private val viewModel by viewModels<ImportViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentImportBinding.bind(view)

        ImagePicker.with(this)
            .cameraOnly()
            .createIntent {
                qrResultListener.launch(it)
            }
    }

    private val qrResultListener =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            if (resultCode == Activity.RESULT_OK) {
                // Image Uri will not be null for RESULT_OK
                val imageUri = data?.data!!
                viewModel.tryToRecogniseQrCode(requireContext(), imageUri, this::onQrScanned, this::onQrNotFound)
            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                viewModel.toast(ImagePicker.getError(data), Toast.LENGTH_LONG)
            }
        }

    private fun onQrScanned(text: String) {
        val splitText = text.split("||")
        val libpasses = mutableListOf<LibraryPass>()

        splitText.forEach {
            val splitString = it.split("|")
            val libpass = LibraryPass(splitString[0], splitString[1])
            libpasses.add(libpass)
        }

        viewModel.saveLibpassesToDatabase(libpasses)

        runOnUiThread {
            findNavController().popBackStack()
        }
    }

    private fun onQrNotFound() {
        findNavController().popBackStack()
    }
}
