package dev.maxsiomin.libpass.fragments.importfragment

import dagger.hilt.android.lifecycle.HiltViewModel
import dev.maxsiomin.libpass.database.LibraryPass
import dev.maxsiomin.libpass.fragments.base.BaseViewModel
import dev.maxsiomin.libpass.util.UiActions
import dev.maxsiomin.libpass.util.runOnIoCoroutine
import javax.inject.Inject

@HiltViewModel
class ImportViewModel @Inject constructor(uiActions: UiActions) : BaseViewModel(uiActions) {

    fun saveLibpassesToDatabase(libpasses: List<LibraryPass>) {
        runOnIoCoroutine {
            libpasses.forEach {
                dao.insertPass(it)
            }
        }
    }
}
