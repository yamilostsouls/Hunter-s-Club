package com.app.huntersclub.ui.lobbies

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.huntersclub.data.repository.UserRepository
import com.app.huntersclub.model.Lobby
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class LobbyListViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val userRepo = UserRepository()

    private val _lobbies = MutableLiveData<List<Lobby>>()
    val lobbies: LiveData<List<Lobby>> = _lobbies

    fun loadRecentLobbies() {
        cleanupExpiredLobbies()

        db.collection("lobbies")
            .whereEqualTo("isOpen", true)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, _ ->
                if (snapshot == null) {
                    _lobbies.value = emptyList()
                    return@addSnapshotListener
                }

                val lobbyList = snapshot.toObjects(Lobby::class.java).toList()
                resolveUsernames(lobbyList)
            }
    }

    /**
     * Resolve usernames for each lobby hostId using UserRepository
     */
    private fun resolveUsernames(lobbies: List<Lobby>) {
        if (lobbies.isEmpty()) {
            _lobbies.value = emptyList()
            return
        }

        var pending = lobbies.size

        lobbies.forEach { lobby ->
            userRepo.getUsername(lobby.hostId) { username ->
                lobby.hostName = username
                pending--

                if (pending == 0) {
                    _lobbies.value = lobbies
                }
            }
        }
    }

    private fun cleanupExpiredLobbies() {
        val now = Timestamp.now()

        db.collection("lobbies")
            .whereLessThan("expiresAt", now)
            .get()
            .addOnSuccessListener { snapshot ->
                snapshot.documents.forEach { doc ->

                    // Delete messages
                    doc.reference.collection("messages")
                        .get()
                        .addOnSuccessListener { messages ->
                            messages.documents.forEach { msg ->
                                msg.reference.delete()
                            }
                        }

                    // Delete lobby
                    doc.reference.delete()
                }
            }
    }
}
