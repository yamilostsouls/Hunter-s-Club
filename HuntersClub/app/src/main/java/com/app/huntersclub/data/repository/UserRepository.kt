package com.app.huntersclub.data.repository

import com.google.firebase.firestore.FirebaseFirestore

class UserRepository {

    private val db = FirebaseFirestore.getInstance()
    private val userCache = mutableMapOf<String, String>()

    fun getUsername(userId: String, callback: (String) -> Unit) {
        if (userId.isBlank()) {
            callback("Desconocido")
            return
        }

        // Cache hit
        userCache[userId]?.let {
            callback(it)
            return
        }

        // Fetch from Firestore
        db.collection("users").document(userId).get()
            .addOnSuccessListener { doc ->
                val name = doc.getString("name") ?: "Desconocido"
                userCache[userId] = name
                callback(name)
            }
            .addOnFailureListener {
                callback("Desconocido")
            }
    }
}
