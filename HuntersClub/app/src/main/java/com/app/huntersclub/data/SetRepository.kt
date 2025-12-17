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
    //Refactored so it orders by username, then by weapon rarity
    fun listenToSets() {
        db.collection("sets")
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    _sets.value = emptyList()
                    return@addSnapshotListener
                }

                val setsList = mutableListOf<Set>()
                if (snapshots != null) {
                    var pending = snapshots.size()
                    for (doc in snapshots) {
                        val setDoc = doc.data.mapValues { it.value.toString() }
                        val userId = setDoc["userId"]
                        val onResolved: (String) -> Unit = { userName ->
                            setsList.add(resolveSet(setDoc, userName))
                            pending--

                            if (pending == 0) {
                                _sets.value = setsList.sortedWith(
                                    compareBy<Set> { it.createdBy.lowercase() }
                                        .thenByDescending { it.weaponRarity }
                                )
                            }
                        }

                        if (!userId.isNullOrBlank()) {
                            db.collection("users").document(userId).get()
                                .addOnSuccessListener { userDoc ->
                                    onResolved(userDoc.getString("name") ?: "Desconocido")
                                }
                                .addOnFailureListener {
                                    onResolved("Desconocido")
                                }
                        } else {
                            onResolved("Desconocido")
                        }
                    }
                }
            }
    }

    //This converts the saved ids on Firebase into a set with names using existing Set model and adapter
    //So in a future we can increase it with more data like damage, defense, rarity, skills, decorations...
    //Images added to the sets
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
            weaponRarity = weapon?.rarity ?: 0,
            weaponType = weapon?.weaponType ?: "gs",
            armorHead = head?.name ?: "Sin casco",
            armorHeadRarity = head?.rarity ?: 0,
            armorHeadType = head?.armorType ?: "head",
            armorChest = chest?.name ?: "Sin pechera",
            armorChestRarity = chest?.rarity ?: 0,
            armorChestType = chest?.armorType ?: "chest",
            armorArms = arms?.name ?: "Sin guantes",
            armorArmsRarity = arms?.rarity ?: 0,
            armorArmsType = arms?.armorType ?: "arms",
            armorWaist = waist?.name ?: "Sin cadera",
            armorWaistRarity = waist?.rarity ?: 0,
            armorWaistType = waist?.armorType ?: "waist",
            armorLegs = legs?.name ?: "Sin piernas",
            armorLegsRarity = legs?.rarity ?: 0,
            armorLegsType = legs?.armorType ?: "legs",
            charm = charm?.name ?: "Sin cigua",
            charmRarity = charm?.rarity ?: 0,
            decorations = emptyList(),
            createdBy = userName
        )
    }
}