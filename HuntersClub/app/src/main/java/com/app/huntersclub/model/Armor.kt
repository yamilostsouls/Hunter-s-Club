package com.app.huntersclub.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Armor (
    val id: Int,
    val name: String,
    val imageArmor: String,
    val rarity: Int,
    val rank: String,
    val armorType: String,
    val slot1: Int,
    val slot2: Int,
    val slot3: Int,
    val defense: Int,
    val fire: Int,
    val water: Int,
    val thunder: Int,
    val ice: Int,
    val dragon: Int,
    val skills: List<Skill> //Armors can have more than 1 skill
):Parcelable