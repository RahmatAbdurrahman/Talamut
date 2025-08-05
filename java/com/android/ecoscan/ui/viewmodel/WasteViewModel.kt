package com.android.ecoscan.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.ecoscan.data.db.Waste
import com.android.ecoscan.data.repository.WasteRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class WasteViewModel(private val repository: WasteRepository) : ViewModel() {

    val allWaste: StateFlow<List<Waste>> = repository.allWaste
        .map { it } // Kalau perlu ubah/urut, lakukan di sini
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun insertWaste(waste: Waste) {
        viewModelScope.launch {
            repository.insert(waste)
        }
    }

    fun deleteWaste(waste: Waste) {
        viewModelScope.launch {
            repository.delete(waste)
        }
    }

    fun clearAllWaste() {
        viewModelScope.launch {
            repository.clearAll()
        }
    }

    val countOrganik: LiveData<Int> = repository.countOrganik()
    val countAnorganik: LiveData<Int> = repository.countAnorganik()
    val countB3: LiveData<Int> = repository.countB3()
}


