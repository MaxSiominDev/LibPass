package dev.maxsiomin.libpass.fragments.base

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.maxsiomin.libpass.util.SharedPrefsConfig
import dev.maxsiomin.libpass.util.UiActions
import javax.inject.Inject

/**
 * If a fragment or an activity uses only UiActions impl, don't create custom viewModel for it, use this
 */
@HiltViewModel
open class BaseViewModel @Inject constructor(uiActions: UiActions) : ViewModel(), UiActions by uiActions {

    suspend fun resaveDefaultLibpass(pId: Int? = null) {
        (pId ?: dao.getLibpassWithLeastId()?.id)?.let {
            sharedPrefs.edit().putInt(SharedPrefsConfig.DEFAULT_LIBPASS_ID, it).apply()
        }
    }

}
