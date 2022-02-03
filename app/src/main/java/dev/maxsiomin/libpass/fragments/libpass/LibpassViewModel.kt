package dev.maxsiomin.libpass.fragments.libpass

import dagger.hilt.android.lifecycle.HiltViewModel
import dev.maxsiomin.libpass.database.LibraryPass
import dev.maxsiomin.libpass.fragments.base.BaseViewModel
import dev.maxsiomin.libpass.util.SharedPrefsConfig.DEFAULT_LIBPASS_ID
import dev.maxsiomin.libpass.util.UiActions
import dev.maxsiomin.libpass.util.runOnIoCoroutine
import javax.inject.Inject

@HiltViewModel
class LibpassViewModel @Inject constructor(uiActions: UiActions) : BaseViewModel(uiActions) {

    fun checkForLibpass(libpassLoadedCallback: (String) -> Unit, emptyDatabaseCallback: () -> Unit) {
        runOnIoCoroutine launch@ {
            val libpassId = sharedPrefs.getInt(DEFAULT_LIBPASS_ID, 1)
            var libpass: LibraryPass? = dao.getLibpassById(libpassId)

            if (libpass != null) {
                libpassLoadedCallback(libpass.value)
                return@launch
            }

            val allLibpasses = dao.loadAllPasses()
            if (!allLibpasses.isNullOrEmpty()) {
                libpass = allLibpasses[0]
                sharedPrefs.edit().putInt(DEFAULT_LIBPASS_ID, libpass.id).apply()
                libpassLoadedCallback(libpass.value)
                return@launch
            }

            emptyDatabaseCallback()
        }
    }
}
