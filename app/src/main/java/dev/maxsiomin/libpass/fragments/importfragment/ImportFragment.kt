package dev.maxsiomin.libpass.fragments.importfragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import dagger.hilt.android.AndroidEntryPoint
import dev.maxsiomin.libpass.R
import dev.maxsiomin.libpass.activity.CaptureActivityPortrait
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
        scan()
    }

    private fun scan() {
        val options = ScanOptions()
            .setBeepEnabled(false)
            .setOrientationLocked(true)
            .setCaptureActivity(CaptureActivityPortrait::class.java)

        barcodeLauncher.launch(options)
    }

    private val barcodeLauncher = registerForActivityResult(ScanContract()) { result ->
        onQrScanned(result.contents)
    }

    private fun onQrScanned(content: String) {
        val splitContent = content.split("||")
        val libpasses = mutableListOf<LibraryPass>()

        try {
            splitContent.forEach {
                val splitString = it.split("|")
                val libpass = LibraryPass(splitString[0], splitString[1])
                libpasses.add(libpass)
            }

            viewModel.saveLibpassesToDatabase(libpasses)

            runOnUiThread {
                findNavController().popBackStack()
            }
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Wrong QR", Toast.LENGTH_SHORT).show()
        }
    }
}
