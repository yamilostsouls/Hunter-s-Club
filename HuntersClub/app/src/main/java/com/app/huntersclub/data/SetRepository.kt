package com.app.huntersclub.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.app.huntersclub.model.Set
import com.google.firebase.firestore.FirebaseFirestore

class SetRepository(
    private val weaponDao: WeaponDAO,
    private val armorDao: ArmorDAO,
    private val charmDao: CharmDAO
) {

    private val db = FirebaseFirestore.getInstance()

    private val _sets = MutableLiveData<List<Set>>()
    val sets: LiveData<List<Set>> get() = _sets

    //Listens the sets collection on Firebase to load them in GalleryFragment section
    //GalleryFragment in this project is the starting screen for the Sets section
    fun listenToSets() {
        db.collection("sets")
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    _sets.value = emptyList()
                    return@addSnapshotListener
                }

                val setsList = mutableListOf<Set>()
                if (snapshots != null) {
                    for (doc in snapshots) {
                        val setDoc = doc.data.mapValues { it.value.toString() }
                        val userId = setDoc["userId"]
                        //We check the user to load it in the set collection
                        if (!userId.isNullOrBlank()) {
                            db.collection("users").document(userId).get()
                                .addOnSuccessListener { userDoc ->
                                    val userName = userDoc.getString("name") ?: "Desconocido"
                                    val resolved = resolveSet(setDoc, userName)
                                    setsList.add(resolved)
                                    _sets.value = setsList
                                }
                                .addOnFailureListener {
                                    val resolved = resolveSet(setDoc, "Desconocido")
                                    setsList.add(resolved)
                                    _sets.value = setsList
                                }
                        } else {
                            val resolved = resolveSet(setDoc, "Desconocido")
                            setsList.add(resolved)
                            _sets.value = setsList
                        }
                    }
                }
            }
    }

    //This converts the saved ids on Firebase into a set with names using existing Set model and adapter
    //So in a future we can increase it with more data like damage, defense, rarity, skills, decorations...
    private fun resolveSet(setDoc: Map<String, String>, userName: String): Set {
        val weapon = setDoc["weapon"]?.toIntOrNull()?.let { weaponDao.getWeaponById(it) }
        val head = setDoc["head"]?.toIntOrNull()?.let { armorDao.getArmorById(it) }
        val chest = setDoc["torso"]?.toIntOrNull()?.let { armorDao.getArmorById(it) }
        val arms = setDoc["arms"]?.toIntOrNull()?.let { armorDao.getArmorById(it) }
        val waist = setDoc["waist"]?.toIntOrNull()?.let { armorDao.getArmorById(it) }
        val legs = setDoc["legs"]?.toIntOrNull()?.let { armorDao.getArmorById(it) }
        val charm = setDoc["charm"]?.toIntOrNull()?.let { charmDao.getCharmById(it) }


        return Set(
            weaponName = weapon?.name ?: "Sin arma",
            armorHead = head?.name ?: "Sin casco",
            armorChest = chest?.name ?: "Sin pechera",
            armorArms = arms?.name ?: "Sin guantes",
            armorWaist = waist?.name ?: "Sin cadera",
            armorLegs = legs?.name ?: "Sin piernas",
            charm = charm?.name ?: "Sin cigua",
            decorations = emptyList(),
            createdBy = userName
        )
    }
}