package com.app.huntersclub.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Skill(
    val name: String,
    val level: Int
) : Parcelable