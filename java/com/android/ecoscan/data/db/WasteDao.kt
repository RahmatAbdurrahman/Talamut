package com.android.ecoscan.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface WasteDao {

    @Query("SELECT * FROM waste_table ORDER BY date DESC")
    fun getAllWaste(): Flow<List<Waste>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWaste(waste: Waste)

    @Delete
    suspend fun deleteWaste(waste: Waste)

    @Query("DELETE FROM waste_table")
    suspend fun clearAllWaste()

    @Query("SELECT COUNT(*) FROM waste_table WHERE type = 'Organik'")
    fun countOrganik(): LiveData<Int>

    @Query("SELECT COUNT(*) FROM waste_table WHERE type = 'Anorganik'")
    fun countAnorganik(): LiveData<Int>

    @Query("SELECT COUNT(*) FROM waste_table WHERE type = 'B3'")
    fun countB3(): LiveData<Int>

    @Update
    suspend fun updateWaste(waste: Waste)

}
