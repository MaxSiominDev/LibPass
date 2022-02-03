package dev.maxsiomin.libpass.fragments.add

import android.content.Context
import android.net.Uri
import android.widget.Toast
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.maxsiomin.libpass.R
import dev.maxsiomin.libpass.fragments.base.BaseViewModel
import dev.maxsiomin.libpass.util.UiActions
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class AddViewModel @Inject constructor(uiActions: UiActions) : BaseViewModel(uiActions) {

    fun tryToRecogniseBarcode(context: Context, imageUri: Uri, onResult: (String) -> Unit) {
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
            .addOnSuccessListener { barcodes ->
                if (barcodes.isEmpty()) {
                    onFailure()
                    return@addOnSuccessListener
                }

                val barcodeValue = barcodes[0].displayValue
                if (barcodeValue == null) {
                    onFailure()
                } else {
                    if (barcodeValue.length == 13) {
                        onResult(barcodeValue)
                        return@addOnSuccessListener
                    } else {
                        val difference = 13 - barcodeValue.length
                        onResult("0".repeat(difference) + barcodeValue)
                    }
                }
            }

            .addOnFailureListener {
                onFailure()
            }
    }

    private fun onFailure() {
        toast(R.string.nothing_found, Toast.LENGTH_LONG)
    }
}
