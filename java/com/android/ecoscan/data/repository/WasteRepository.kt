package com.android.ecoscan.data.repository

import androidx.lifecycle.LiveData
import com.android.ecoscan.data.db.Waste
import com.android.ecoscan.data.db.WasteDao
import kotlinx.coroutines.flow.Flow

class WasteRepository(private val wasteDao: WasteDao) {

    val allWaste: Flow<List<Waste>> = wasteDao.getAllWaste()

    suspend fun insert(waste: Waste) {
        wasteDao.insertWaste(waste)
    }

    suspend fun delete(waste: Waste) {
        wasteDao.deleteWaste(waste)
    }

    suspend fun clearAll() {
        wasteDao.clearAllWaste()
    }

    fun countOrganik(): LiveData<Int> = wasteDao.countOrganik()

    fun countAnorganik(): LiveData<Int> = wasteDao.countAnorganik()

    fun countB3(): LiveData<Int> = wasteDao.countB3()

    companion object {
        @Volatile
        private var INSTANCE: WasteRepository? = null

        fun getInstance(wasteDao: WasteDao): WasteRepository {
            return INSTANCE ?: synchronized(this) {
                val instance = WasteRepository(wasteDao)
                INSTANCE = instance
                instance
            }
        }
    }
}
