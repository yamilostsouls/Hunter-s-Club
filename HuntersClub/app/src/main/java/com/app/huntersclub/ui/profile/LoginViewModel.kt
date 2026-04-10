package com.app.huntersclub.ui.profile

import android.app.Application
import androidx.lifecycle.*
import com.app.huntersclub.HuntersClubApp
import com.app.huntersclub.utils.UserSession
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginViewModel(app: Application) : AndroidViewModel(app) {

    private val auth: FirebaseAuth = (app as HuntersClubApp).auth
    private val db = FirebaseFirestore.getInstance()

    private val _loginResult = MutableLiveData<Boolean>()
    val loginResult: LiveData<Boolean> = _loginResult

    private val _userDataLoaded = MutableLiveData<Boolean>()
    val userDataLoaded: LiveData<Boolean> = _userDataLoaded

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    fun login(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            _errorMessage.value = "El correo y la contraseña no pueden estar vacíos."
            _loginResult.value = false
            return
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _loginResult.value = true
                } else {
                    _loginResult.value = false
                    _errorMessage.value = task.exception?.message
                }
            }
    }

    fun preloadUserData() {
        val userId = auth.currentUser?.uid ?: run {
            _userDataLoaded.value = true
            return
        }

        db.collection("users").document(userId)
            .get()
            .addOnSuccessListener { snapshot ->
                if (snapshot != null && snapshot.exists()) {

                    val name = snapshot["name"]?.toString() ?: "Usuario"
                    val image = snapshot["profileImage"]?.toString() ?: ""

                    UserSession.setUserData(name, image)
                }
                _userDataLoaded.value = true
            }
            .addOnFailureListener {
                _userDataLoaded.value = true
            }
    }
}
