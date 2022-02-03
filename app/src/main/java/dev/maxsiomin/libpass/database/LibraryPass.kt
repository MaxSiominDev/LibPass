package dev.maxsiomin.libpass.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.maxsiomin.libpass.database.LibpassDatabase.Companion.LIBPASS_ALIAS
import dev.maxsiomin.libpass.database.LibpassDatabase.Companion.LIBPASS_TABLE
import dev.maxsiomin.libpass.database.LibpassDatabase.Companion.LIBPASS_VALUE

@Entity(tableName = LIBPASS_TABLE)
data class LibraryPass (
    @PrimaryKey(autoGenerate = true)
    val id: Int,

    @ColumnInfo(name = LIBPASS_VALUE)
    val value: String,

    @ColumnInfo(name = LIBPASS_ALIAS)
    val alias: String,
) {
    constructor(value: String, alias: String) : this(0, value, alias)
}
