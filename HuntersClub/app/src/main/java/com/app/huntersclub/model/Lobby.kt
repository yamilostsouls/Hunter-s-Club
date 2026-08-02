package com.app.huntersclub.model

import com.google.firebase.Timestamp

data class Lobby(
    val sessionId: String = "",
    val hostId: String = "",
    val createdAt: Timestamp = Timestamp.now(),
    val expiresAt: Timestamp = Timestamp.now(),
    val monster: Int? = null,
    val hrRequirement: Int? = null,
    val isOpen: Boolean = true,
    var hostName: String = ""
)
