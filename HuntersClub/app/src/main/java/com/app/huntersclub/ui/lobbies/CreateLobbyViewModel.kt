package com.app.huntersclub.ui.lobbies

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CreateLobbyViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val _createResult = MutableLiveData<Event<Boolean>>()
    val createResult: LiveData<Event<Boolean>> = _createResult
    var lastSessionId: String = ""

    fun createLobby(sessionId: String, monster: Int?, hrRequirement: Int?) {
        val userId = auth.currentUser?.uid ?: return

        val now = Timestamp.now()
        val expiresAt = Timestamp(now.seconds + 86400, 0)


        val lobbyMap = hashMapOf(
            "sessionId" to sessionId,
            "hostId" to userId,
            "createdAt" to now,
            "expiresAt" to expiresAt,
            "monster" to monster,
            "hrRequirement" to hrRequirement,
            "isOpen" to true,
        )

        db.collection("lobbies")
            .document(sessionId)
            .set(lobbyMap).addOnSuccessListener { _createResult.value = Event(true) }
            .addOnFailureListener { _createResult.value = Event(false) }


    }

    class Event<out T>(private val content: T) {
        private var handled = false

        fun getIfNotHandled(): T? {
            return if (handled) null else {
                handled = true
                content
            }
        }
    }

}
