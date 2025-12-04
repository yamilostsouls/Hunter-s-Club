package com.app.huntersclub.model

data class MonsterData (
    val id: Int,
    val name: String,
    val monCategory: String,
    val imagenResId: String,
    val description: String,
    val weaknessFire: Int,
    val weaknessWater: Int,
    val weaknessThunder: Int,
    val weaknessIce: Int,
    val weaknessDragon: Int,
    val weaknessPoison: Int,
    val weaknessSleep: Int,
    val weaknessParalysis: Int,
    val weaknessBlast: Int,
    val weaknessStun: Int,
    val hasAltWeakness: Boolean,
    val altWeaknessFire: Int?,
    val altWeaknessWater: Int?,
    val altWeaknessThunder: Int?,
    val altWeaknessIce: Int?,
    val altWeaknessDragon: Int?,
    val altWeaknessPoison: Int?,
    val altWeaknessSleep: Int?,
    val altWeaknessParalysis: Int?,
    val altWeaknessBlast: Int?,
    val altWeaknessStun: Int?
)

