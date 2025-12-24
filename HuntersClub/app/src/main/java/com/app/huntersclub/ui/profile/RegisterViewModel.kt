package com.app.huntersclub.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterViewModel (
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore
): ViewModel() {

    private val _registerResult = MutableLiveData<Boolean>()
    val registerResult: LiveData<Boolean> = _registerResult

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage
    //Register function to add an user on Firebase database
    //With a restriction of 6 character password
    fun register(name: String, email: String, password: String, confirmPassword: String) {

        if (password.length < 6) {
            _registerResult.value = false
            _errorMessage.value = "La contraseña debe tener al menos 6 caracteres."
            return
        }

        if (password != confirmPassword) {
            _registerResult.value = false
            _errorMessage.value = "Las contraseñas no coinciden."
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid
                    val userMap = hashMapOf(
                        "id" to userId,
                        "email" to email,
                        "name" to name,
                        "profileImage" to ""
                    )
                    db.collection("users").document(userId!!)
                        .set(userMap)
                        .addOnSuccessListener {
                            _registerResult.value = true
                        }
                        .addOnFailureListener { e ->
                            _registerResult.value = false
                            _errorMessage.value = e.message
                        }
                } else {
                    _registerResult.value = false
                    _errorMessage.value = task.exception?.message
                }
            }
    }

}
