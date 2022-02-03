package dev.maxsiomin.libpass.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface LibpassDao {

    /**
     * Returns all passes
     */
    @Query(value = "SELECT * FROM 'libpassTable'")
    suspend fun loadAllPasses(): List<LibraryPass>?

    /**
     * Inserts an [element] to [LibpassDatabase]
     */
    @Insert
    suspend fun insertPass(element: LibraryPass)

    /**
     * Get the first added libpass of libpasses that were not deleted yet
     */
    @Query(value = "SELECT * FROM 'libpassTable' WHERE id=(SELECT MIN(id) FROM 'libpassTable')")
    suspend fun getLibpassWithLeastId(): LibraryPass?

    /**
     * Returns Int value that represents how many elements are currently in table
     */
    @Query(value = "SELECT COUNT(id) FROM 'libpassTable'")
    suspend fun getTableLength(): Int?

    /**
     * Removes all passes
     */
    @Query(value = "DELETE FROM 'libpassTable'")
    suspend fun clearDatabase()


    @Query(value = "DELETE FROM 'libpassTable' WHERE id=:desiredId")
    suspend fun deleteLibpass(desiredId: Int)

    /**
     * Returns libpass with particular id
     */
    @Query(value = "SELECT * FROM 'libpassTable' WHERE id=:desiredId")
    suspend fun getLibpassById(desiredId: Int): LibraryPass?
}

