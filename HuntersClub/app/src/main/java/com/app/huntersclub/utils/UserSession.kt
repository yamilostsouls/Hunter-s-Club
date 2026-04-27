package com.app.huntersclub.utils

object UserSession {

    @Volatile
    private var _userName: String = ""

    @Volatile
    private var _profileImage: String = ""

    val userName: String
        get() = _userName

    val profileImage: String
        get() = _profileImage

    fun setUserData(name: String, image: String) {
        _userName = name
        _profileImage = image
    }

    fun clear() {
        _userName = ""
        _profileImage = ""
    }
}

