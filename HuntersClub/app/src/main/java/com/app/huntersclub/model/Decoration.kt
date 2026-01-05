package com.app.huntersclub.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Decoration (
    val id: Int,
    val name: String,
    val imageDeco: String,
    val slot: Int,
    val rarity: Int,
    val skills: List<Skill> //Decorations have at least 1 skill up to 2 skills
):Parcelable
