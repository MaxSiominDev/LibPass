package dev.maxsiomin.libpass.fragments.passes

import androidx.lifecycle.MutableLiveData
import dev.maxsiomin.libpass.database.LibraryPass
import dev.maxsiomin.libpass.util.UiActions
import dev.maxsiomin.libpass.util.runOnIoCoroutine

/**
 * Provides passes from database
 */
class DatabaseLoader(uiActions: UiActions) : UiActions by uiActions {

    val itemsLiveData = MutableLiveData<List<PlaceholderItem>>()
    private lateinit var items: MutableList<PlaceholderItem>

    init {
        runOnIoCoroutine {
            getPasses()
        }
    }

    /**
     * Loads all passes from Database and updates [itemsLiveData]
     */
    suspend fun getPasses() {
        val passes: List<LibraryPass>? = dao.loadAllPasses()
        if (passes == null) {
            itemsLiveData.postValue(listOf())
            return
        }

        items = mutableListOf()
        var pass: LibraryPass
        for (i in passes.indices.reversed()) {
            pass = passes[i]
            items.add(createPlaceholderItem(pass.id, pass.alias, pass.value))
        }

        itemsLiveData.postValue(items)
    }

    /**
     * Returns new [PlaceholderItem]
     */
    private fun createPlaceholderItem(id: Int, alias: String, value: String) =
        PlaceholderItem(id, alias, value)

    /**
     * A placeholder item representing a piece of content
     */
    data class PlaceholderItem(val id: Int, val alias: String, val value: String)
}
