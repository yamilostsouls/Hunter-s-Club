package com.app.huntersclub

import android.app.Application
import com.app.huntersclub.data.dao.ArmorDAO
import com.app.huntersclub.data.dao.CharmDAO
import com.app.huntersclub.data.dao.DecoDAO
import com.app.huntersclub.data.dao.WeaponDAO
import com.app.huntersclub.data.database.MyDatabaseHelper
import com.app.huntersclub.data.repository.SetRepository
import com.app.huntersclub.utils.DecoDrawableCache.preloadAllDecorations
import com.google.firebase.auth.FirebaseAuth

class HuntersClubApp : Application() {

    lateinit var setRepository: SetRepository
        private set

    lateinit var auth: FirebaseAuth
        private set


    override fun onCreate() {
        super.onCreate()

        preloadAllDecorations(this)

        auth = FirebaseAuth.getInstance()

        val dbHelper = MyDatabaseHelper(this)
        val weaponDao = WeaponDAO(dbHelper)
        val armorDao = ArmorDAO(dbHelper)
        val charmDao = CharmDAO(dbHelper)
        val decoDao = DecoDAO(dbHelper)

        setRepository = SetRepository(weaponDao, armorDao, charmDao, decoDao)
    }
}

