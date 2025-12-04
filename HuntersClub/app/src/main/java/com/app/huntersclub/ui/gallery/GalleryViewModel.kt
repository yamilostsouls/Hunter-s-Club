package com.app.huntersclub.ui.gallery

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.app.huntersclub.data.SetRepository
import com.app.huntersclub.model.Set

class GalleryViewModel(
    private val setRepository: SetRepository
) : ViewModel() {

    //LiveData that retrieves the Set list from Firebase
    val sets: LiveData<List<Set>> = setRepository.sets

    //Function that starts the real time listening
    fun listenToSets() {
        setRepository.listenToSets()
    }
}
