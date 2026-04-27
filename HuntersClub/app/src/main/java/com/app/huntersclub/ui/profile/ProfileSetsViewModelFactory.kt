package com.app.huntersclub.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.app.huntersclub.data.repository.SetRepository

class ProfileSetsViewModelFactory(
    private val repository: SetRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass == ProfileSetsViewModel::class.java) {
            @Suppress("UNCHECKED_CAST")
            return ProfileSetsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
