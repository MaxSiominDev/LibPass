package dev.maxsiomin.libpass.extensions

import android.text.Editable
import android.text.SpannableStringBuilder

/**
 * If string == null, returns ""
 */
fun String?.notNull(): String = this ?: ""

fun String.toEditable(): Editable = SpannableStringBuilder(this)

fun String.splitBySpace(): List<String> = this.split(" ")
