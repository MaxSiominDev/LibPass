package dev.maxsiomin.libpass.fragments.add

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import dagger.hilt.android.AndroidEntryPoint
import dev.maxsiomin.libpass.R
import dev.maxsiomin.libpass.activity.CaptureActivityPortrait
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentAddBinding.bind(view)

        binding.apply {
            buttonScan.setOnClickListener {
                scan()
            }

            textViewManually.setOnClickListener {
                findNavController().navigate(R.id.action_addFragment_to_enterManuallyFragment)
            }
        }
    }

    private fun scan() {
        val options = ScanOptions()
            .setBeepEnabled(false)
            .setOrientationLocked(true)
            .setCaptureActivity(CaptureActivityPortrait::class.java)

        barcodeLauncher.launch(options)
    }

    private val barcodeLauncher = registerForActivityResult(ScanContract()) { result ->
        onBarcodeLoaded(result.contents)
    }

    private fun onBarcodeLoaded(content: String) {
        val direction = AddFragmentDirections.actionAddFragmentToConfirmFragment(content)
        findNavController().navigate(direction)
    }
}
