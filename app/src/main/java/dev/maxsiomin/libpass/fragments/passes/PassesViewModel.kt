package dev.maxsiomin.libpass.fragments.passes

import dagger.hilt.android.lifecycle.HiltViewModel
import dev.maxsiomin.libpass.fragments.base.BaseViewModel
import dev.maxsiomin.libpass.util.UiActions
import javax.inject.Inject

@HiltViewModel
class PassesViewModel @Inject constructor(uiActions: UiActions) : BaseViewModel(uiActions) {

    suspend fun deleteLibpass(id: Int) {
        dao.deleteLibpass(id)
        resaveDefaultLibpass()
    }
}
