package com.app.huntersclub.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.app.huntersclub.data.dao.ArmorDAO
import com.app.huntersclub.data.dao.CharmDAO
import com.app.huntersclub.data.dao.DecoDAO
import com.app.huntersclub.data.dao.WeaponDAO
import com.app.huntersclub.model.Set
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class SetRepository(
    private val weaponDao: WeaponDAO,
    private val armorDao: ArmorDAO,
    private val charmDao: CharmDAO,
    private val decoDao: DecoDAO
) {

    private val db = FirebaseFirestore.getInstance()

    private val _sets = MutableLiveData<List<Set>>()
    val sets: LiveData<List<Set>> get() = _sets
    //Cache preload so the first time loading the list
    //of sets is not that slow as using getXById
    private val weaponsCache by lazy {
        weaponDao.getAllWeapons().associateBy { it.id }
    }
    private val armorCache by lazy {
        armorDao.getAllArmor().associateBy { it.id }
    }
    private val charmsCache by lazy {
        charmDao.getAllCharms().associateBy { it.id }
    }
    private val decoCache by lazy {
        decoDao.getAllDecorations().associateBy { it.id }
    }

    private val userCache = mutableMapOf<String, String>()
    //Listens the sets collection on Firebase to load them in SetsFragment section
    //listenToSets() just listens to the Firebase data and hops onto processSnapshots()
    fun listenToSets() {
        db.collection("sets")
            .addSnapshotListener { snapshots, e ->
                if (e != null || snapshots == null) {
                    _sets.value = emptyList()
                    return@addSnapshotListener
                }
                userCache.clear()
                processSnapshots(snapshots)
            }
    }
    //Separated functions to refactor listenToSets and lower its complexity
    //Function that manages the sets of our database and hops on resolveUsername() and sortSets()
    private fun processSnapshots(snapshots: Iterable<DocumentSnapshot>) {
        val setsList = mutableListOf<Set>()
        var pending = snapshots.count()
        for (doc in snapshots) {
            resolveUsername(doc) { userName ->
                setsList.add(resolveSet(doc, userName))
                pending--

                if (pending == 0) {
                    _sets.value = sortSets(setsList)
                }
            }
        }
    }
    //Function to encapsulate the logic for the users (the Set created by)
    private fun resolveUsername(doc: DocumentSnapshot, callback: (String) -> Unit) {
        val userId = doc.getString("userId")
        if (userId.isNullOrBlank()) {
            callback("Desconocido")
            return
        }
        userCache[userId]?.let { cached ->
            callback(cached)
            return
        }
        db.collection("users").document(userId).get()
            .addOnSuccessListener { userDoc ->
                val name = userDoc.getString("name") ?: "Desconocido"
                userCache[userId] = name
                callback(name)
            }
            .addOnFailureListener {
                callback("Desconocido")
            }
    }
    //Function to sort the sets by username and weapon rarity
    private fun sortSets(list: List<Set>): List<Set> =
        list.sortedWith(
            compareBy<Set> { it.createdBy.lowercase() }
                .thenByDescending { it.weaponRarity }
        )

    //This converts the saved ids on Firebase into a set with names using existing Set model and adapter
    //So in a future we can increase it with more data like damage, defense, rarity, skills, decorations...
    //Refactor to read the decorations and not break the sets
    private fun resolveSet(doc: DocumentSnapshot, userName: String): Set {
        val name = doc.getString("name")
        val weapon = doc.getLong("weapon")?.toInt()?.let { weaponsCache[it] }
        val head = doc.getLong("head")?.toInt()?.let { armorCache[it] }
        val chest = doc.getLong("torso")?.toInt()?.let { armorCache[it] }
        val arms = doc.getLong("arms")?.toInt()?.let { armorCache[it] }
        val waist = doc.getLong("waist")?.toInt()?.let { armorCache[it] }
        val legs = doc.getLong("legs")?.toInt()?.let { armorCache[it] }
        val charm = doc.getLong("charm")?.toInt()?.let { charmsCache[it] }

        val decorationsRaw = (doc["decorations"] as? Map<*, *>)
            ?.mapNotNull { (k, v) ->
                (k as? String)?.let { it to v }
            }
            ?.toMap()
            ?: emptyMap()

        val decorations = decorationsRaw.mapNotNull { (slotKey, decoIdAny) ->
            val decoId = decoIdAny.toString().toIntOrNull()
            val deco = decoId?.let { decoCache[it] }
            if (deco != null) slotKey to deco else null
        }.toMap()

        return Set(
            setName = name?: "Set genérico",
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
            decorations = decorations,
            createdBy = userName
        )
    }
}
