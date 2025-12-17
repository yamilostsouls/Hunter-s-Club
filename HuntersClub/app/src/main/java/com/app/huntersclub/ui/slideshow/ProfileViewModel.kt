package com.app.huntersclub.ui.slideshow


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.huntersclub.utils.SingleLiveEvent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    private val _userName = MutableLiveData<String>()
    val userName: LiveData<String> = _userName

    private val _profileImage = MutableLiveData<String>()
    val profileImage: LiveData<String> = _profileImage

    private val _updateResult = SingleLiveEvent<Boolean>()
    val updateResult: LiveData<Boolean> = _updateResult

    private val _logoutResult = MutableLiveData<Boolean>()
    val logoutResult: LiveData<Boolean> = _logoutResult

    private val _errorMessage = MutableLiveData<String?>()
    //val errorMessage: LiveData<String?> = _errorMessage

    fun loadUserData() {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            db.collection("users").document(userId)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        _userName.value = document.getString("name") ?: "Usuario"
                        _profileImage.value = document.getString("profileImage") ?: ""
                    }
                }
        }
    }
    //Update profile with 2 conditions:
    //1. Name can't be empty
    //2. User has to choose one of the avatars provided
    fun updateProfile(newName: String, avatarFileName: String) {
        val userId = auth.currentUser?.uid

        if (newName.isBlank()) {
            _updateResult.value = false
            _errorMessage.value = "El nombre no puede estar vacío"
            return
        }

        if(avatarFileName.isBlank()){
            _updateResult.value = false
            _errorMessage.value = "Selecciona una imagen."
            return
        }

        if (userId != null) {
            val updates = hashMapOf<String, Any>(
                "name" to newName,
                "profileImage" to avatarFileName
            )
            db.collection("users").document(userId)
                .update(updates)
                .addOnSuccessListener {
                    _userName.value = newName
                    _profileImage.value = avatarFileName
                    _updateResult.value = true
                }
                .addOnFailureListener {
                    _updateResult.value = false
                    _errorMessage.value = it.message
                }
        }
    }

    fun logout() {
        auth.signOut()
        _logoutResult.value = true
    }

}
