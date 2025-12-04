package com.app.huntersclub.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Weapon (
    val id: Int,
    val name: String,
    val imageCharm: String,
    val rarity: Int,
    val weaponType: String,
    val atk: Int,
    val affinity: Int,
    val def: Int,
    val slot1: Int,
    val slot2: Int,
    val slot3: Int,
    val elementHidden: Boolean,
    val element1: String?,
    val element1Dmg: Int?,
    val element2: String?,
    val element2Dmg: Int?,
    val elderSeal: String?,
    val sharpness: List<Int>?,
    val armorSetBonusId: Int?,
    val skillName: String?,
    val skillLevel: Int?
):Parcelable