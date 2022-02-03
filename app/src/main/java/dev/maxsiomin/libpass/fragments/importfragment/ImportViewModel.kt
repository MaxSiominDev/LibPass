package dev.maxsiomin.libpass.fragments.importfragment

import android.content.Context
import android.net.Uri
import android.widget.Toast
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.maxsiomin.libpass.R
import dev.maxsiomin.libpass.database.LibraryPass
import dev.maxsiomin.libpass.fragments.base.BaseViewModel
import dev.maxsiomin.libpass.util.UiActions
import dev.maxsiomin.libpass.util.runOnIoCoroutine
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class ImportViewModel @Inject constructor(uiActions: UiActions) : BaseViewModel(uiActions) {

    private lateinit var callback: () -> Unit

    fun tryToRecogniseQrCode(context: Context, imageUri: Uri, onSuccess: (String) -> Unit, onFailureCallback: () -> Unit) {
        callback = onFailureCallback

        val image: InputImage
        try {
            image = InputImage.fromFilePath(context, imageUri)
        } catch (e: IOException) {
            Timber.e(e)
            onFailure()
            return
        }

        val scanner = BarcodeScanning.getClient()

        scanner.process(image)
            .addOnSuccessListener { qrCodes ->
                if (qrCodes.isEmpty()) {
                    onFailure()
                    return@addOnSuccessListener
                }

                val qrCodeValue = qrCodes[0].displayValue
                when {
                    qrCodeValue == null -> onFailure()
                    "|" !in qrCodeValue -> onFailure()
                    else -> onSuccess(qrCodeValue)
                }
            }

            .addOnFailureListener {
                onFailure()
            }
    }

    fun saveLibpassesToDatabase(libpasses: List<LibraryPass>) {
        runOnIoCoroutine {
            libpasses.forEach {
                dao.insertPass(it)
            }
        }
    }

    private fun onFailure() {
        toast(R.string.nothing_found, Toast.LENGTH_LONG)
        callback()
    }
}
