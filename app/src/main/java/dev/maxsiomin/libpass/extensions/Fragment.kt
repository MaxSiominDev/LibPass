package dev.maxsiomin.libpass.extensions

import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment

fun Fragment.runOnUiThread(action: () -> Unit) {
    requireActivity().runOnUiThread {
        action()
    }
}

val Fragment.imm: InputMethodManager
    get() = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
