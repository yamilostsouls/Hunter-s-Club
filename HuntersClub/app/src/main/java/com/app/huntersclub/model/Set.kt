package com.app.huntersclub.model

data class Set(
    val id: String="",
    val setName: String = "",
    val weaponName: String = "",
    val weaponRarity: Int,
    val weaponType: String = "",
    val armorHead: String = "",
    val armorHeadRarity: Int,
    val armorHeadType: String = "",
    val armorChest: String = "",
    val armorChestRarity: Int,
    val armorChestType: String = "",
    val armorArms: String = "",
    val armorArmsRarity: Int,
    val armorArmsType: String = "",
    val armorWaist: String = "",
    val armorWaistRarity: Int,
    val armorWaistType: String = "",
    val armorLegs: String = "",
    val armorLegsRarity: Int,
    val armorLegsType: String = "",
    val charm: String = "",
    val charmRarity: Int,
    val decorations: Map<String, Decoration> = emptyMap(),
    val createdBy: String = "",
    val createdById: String = ""
)