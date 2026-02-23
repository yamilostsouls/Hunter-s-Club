package com.app.huntersclub.ui.profile

import androidx.lifecycle.ViewModel
import com.app.huntersclub.data.repository.SetRepository
import com.google.firebase.auth.FirebaseAuth
import androidx.lifecycle.map
import com.app.huntersclub.model.Set

class ProfileSetsViewModel(private val repository: SetRepository) : ViewModel() {

    private val currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

    val userSets = repository.sets.map { list ->
        list.filter { it.createdById == currentUserId }
    }

    fun listenToSets() {
        repository.listenToSets()
    }

    fun deleteSet(set: Set) {
        repository.deleteSet(set)
    }

}
