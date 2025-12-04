package com.app.huntersclub.model

data class Set(
    val weaponName: String = "",
    val armorHead: String = "",
    val armorChest: String = "",
    val armorArms: String = "",
    val armorWaist: String = "",
    val armorLegs: String = "",
    val charm: String = "",
    val decorations: List<String> = emptyList(),
    val createdBy: String = ""
)