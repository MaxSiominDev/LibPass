package dev.maxsiomin.libpass.fragments.add

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
import dev.maxsiomin.libpass.databinding.FragmentAddBinding
import dev.maxsiomin.libpass.fragments.base.BaseFragment

/**
 * User can go here from LibpassFragment.
 * User is to choose way of adding their libpass to the app (manually or scan)
 */
@AndroidEntryPoint
class AddFragment : BaseFragment(R.layout.fragment_add) {

    private lateinit var binding: FragmentAddBinding

    override val mRoot get() = binding.root

    private val viewModel by viewModels<AddViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentAddBinding.bind(view)

        binding.apply {
            buttonScan.setOnClickListener {
                takeBarcodePhoto()
            }

            textViewManually.setOnClickListener {
                findNavController().navigate(R.id.action_addFragment_to_enterManuallyFragment)
            }
        }
    }

    private fun takeBarcodePhoto() {
        ImagePicker.with(this)
            .cameraOnly()
            .createIntent { intent ->
                imageResultListener.launch(intent)
            }
    }

    private val imageResultListener =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            if (resultCode == Activity.RESULT_OK) {
                // Image Uri will not be null for RESULT_OK
                val imageUri = data?.data!!
                viewModel.tryToRecogniseBarcode(requireContext(), imageUri) {
                    val direction = AddFragmentDirections.actionAddFragmentToConfirmFragment(it)
                    findNavController().navigate(direction)
                }
            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                viewModel.toast(ImagePicker.getError(data), Toast.LENGTH_LONG)
            }
        }
}
