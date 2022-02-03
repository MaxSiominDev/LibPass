package dev.maxsiomin.libpass.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun runOnIoCoroutine(action: suspend () -> Unit) {
    CoroutineScope(Dispatchers.IO).launch {
        action()
    }
}
