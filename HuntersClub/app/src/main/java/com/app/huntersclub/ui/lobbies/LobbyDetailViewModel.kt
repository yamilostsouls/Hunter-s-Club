package com.app.huntersclub.ui.lobbies

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.huntersclub.data.repository.UserRepository
import com.app.huntersclub.model.Lobby
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LobbyDetailViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val userRepo = UserRepository()
    private val auth = FirebaseAuth.getInstance()

    private val _lobby = MutableLiveData<Lobby>()
    val lobby: LiveData<Lobby> = _lobby

    private val _members = MutableLiveData<List<String>>()
    val members: LiveData<List<String>> = _members

    private val _memberIds = MutableLiveData<List<String>>()
    val memberIds: LiveData<List<String>> = _memberIds

    private val _memberNames = MutableLiveData<List<String>>()
    val memberNames: LiveData<List<String>> = _memberNames

    private val _lobbyFull = MutableLiveData<Boolean>()
    val lobbyFull: LiveData<Boolean> = _lobbyFull



    fun loadLobby(sessionId: String) {
        db.collection("lobbies")
            .document(sessionId)
            .addSnapshotListener { snapshot, _ ->
                if (snapshot != null && snapshot.exists()) {
                    _lobby.value = snapshot.toObject(Lobby::class.java)
                }
            }
    }

    fun loadMembers(sessionId: String) {
        db.collection("lobbies")
            .document(sessionId)
            .collection("members")
            .orderBy("joinedAt")
            .addSnapshotListener { snap, _ ->
                if (snap == null) {
                    _memberIds.value = emptyList()
                    _memberNames.value = emptyList()
                    return@addSnapshotListener
                }

                val ids = snap.documents.map { it.id }
                _memberIds.value = ids

                if (ids.isEmpty()) {
                    _memberNames.value = emptyList()
                    return@addSnapshotListener
                }

                val names = mutableListOf<String>()
                var pending = ids.size

                ids.forEach { uid ->
                    userRepo.getUsername(uid) { name ->
                        names.add(name)
                        pending--

                        if (pending == 0) {
                            _memberNames.value = names.sorted()
                        }
                    }
                }
            }
    }


    fun joinLobby(sessionId: String) {
        val userId = auth.currentUser?.uid ?: return

        val membersRef = db.collection("lobbies")
            .document(sessionId)
            .collection("members")

        membersRef.get().addOnSuccessListener { snap ->
            val currentCount = snap.size()

            if (currentCount >= 16) {
                _lobbyFull.value = true
                return@addOnSuccessListener
            }

            membersRef.document(userId)
                .set(mapOf("joinedAt" to Timestamp.now()))
        }
    }


    fun leaveLobby(sessionId: String) {
        val userId = auth.currentUser?.uid ?: return

        db.collection("lobbies")
            .document(sessionId)
            .collection("members")
            .document(userId)
            .delete()
    }

}
