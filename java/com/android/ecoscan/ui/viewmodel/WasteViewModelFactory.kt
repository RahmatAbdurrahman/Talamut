package com.android.ecoscan.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.ecoscan.data.repository.WasteRepository

class WasteViewModelFactory(private val repository: WasteRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WasteViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WasteViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
