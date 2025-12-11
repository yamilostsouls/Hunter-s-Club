package com.app.huntersclub.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Charm(
    val id: Int,
    val name: String,
    val imageCharm: String,
    val rarity: Int,
    val skills: List<Skill> //Some charms have more than 1 skill
) : Parcelable
