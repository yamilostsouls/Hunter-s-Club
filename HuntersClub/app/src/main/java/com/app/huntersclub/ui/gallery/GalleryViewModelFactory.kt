package com.app.huntersclub.ui.gallery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.app.huntersclub.data.SetRepository
//"Custom construct" for the Sets
class GalleryViewModelFactory(
    private val setRepository: SetRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GalleryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GalleryViewModel(setRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
