package com.app.huntersclub.ui.sets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.app.huntersclub.data.SetRepository
//"Custom construct" for the Sets
class SetsViewModelFactory(
    private val setRepository: SetRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SetsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SetsViewModel(setRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
