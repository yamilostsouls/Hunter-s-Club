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
    val skillTreeName: String,
    val skillTreeLevel: Int
):Parcelable
