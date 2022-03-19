package dev.maxsiomin.libpass.fragments.edit

import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.maxsiomin.libpass.database.LibraryPass
import dev.maxsiomin.libpass.fragments.base.BaseViewModel
import dev.maxsiomin.libpass.util.SharedPrefsConfig.DEFAULT_LIBPASS_ID
import dev.maxsiomin.libpass.util.UiActions
import dev.maxsiomin.libpass.util.runOnIoCoroutine
import javax.inject.Inject

@HiltViewModel
class EditViewModel @Inject constructor(uiActions: UiActions) : BaseViewModel(uiActions) {

    val libpassLiveData = MutableLiveData<LibraryPass>()

    fun isDefaultLibpass(id: Int): Boolean {
        return sharedPrefs.getInt(DEFAULT_LIBPASS_ID, 1) == id
    }

    fun getLibpassesCount(action: (Int) -> Unit) {
        runOnIoCoroutine {
            action(dao.getTableLength()!!)
        }
    }

    fun loadLibpass(id: Int) {
        runOnIoCoroutine {
            libpassLiveData.postValue(dao.getLibpassById(id))
        }
    }

    fun resaveLibpass(id: Int, value: String, alias: String) {
        runOnIoCoroutine {
            val libpass = LibraryPass(0, value, alias)
            dao.deleteLibpass(id)
            dao.insertPass(libpass)

            if (isDefaultLibpass(id)) {
                sharedPrefs.edit().putInt(DEFAULT_LIBPASS_ID, 0).apply()
            }
        }
    }
}
