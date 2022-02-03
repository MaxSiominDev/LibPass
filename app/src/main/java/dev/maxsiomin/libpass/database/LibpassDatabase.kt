package dev.maxsiomin.libpass.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

private const val VERSION = 1

/**
 * Contains all passes
 */
@Database(entities = [LibraryPass::class], version = VERSION)
abstract class LibpassDatabase : RoomDatabase() {

    /**
     * Returns new instance of [LibpassDao]
     */
    abstract fun libpassDao(): LibpassDao

    companion object {

        private const val DATABASE_NAME = "libpassDatabase"
        const val LIBPASS_TABLE = "libpassTable"
        const val LIBPASS_VALUE = "value"
        const val LIBPASS_ALIAS = "alias"

        @Volatile
        private var INSTANCE: LibpassDatabase? = null

        /**
         * Returns instance of [LibpassDatabase]
         */
        fun getInstance(context: Context): LibpassDatabase {

            synchronized(this) {

                var instance = INSTANCE

                // If instance is `null` make a new database instance
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context,
                        LibpassDatabase::class.java,
                        DATABASE_NAME
                    ).fallbackToDestructiveMigration()
                        .build()

                    INSTANCE = instance
                }

                // Return instance; smart cast to be non-null
                return instance
            }
        }
    }
}
