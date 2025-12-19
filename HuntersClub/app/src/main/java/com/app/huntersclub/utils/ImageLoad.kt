package com.app.huntersclub.utils

object ImageLoad {
    //Function to obtain the images from assets and load them
    fun getAssetPath(type: String, rarity: Int? = null, subtype: String? = null, id: Int? = null, name: String? = null): String {

        return when (type) {
            "charms" -> {
                "file:///android_asset/charms/$rarity.png"
            }
            "weapons" -> {
                //Internal conversion for weapon types
                val prefix = when (subtype) {
                    "great-sword" -> "gs"
                    "long-sword" -> "ls"
                    "sword-and-shield" -> "sas"
                    "dual-blades" -> "db"
                    "hammer" -> "hammer"
                    "hunting-horn" -> "hh"
                    "lance" -> "lance"
                    "gunlance" -> "gl"
                    "switch-axe" -> "sa"
                    "charge-blade" -> "cb"
                    "insect-glaive" -> "ig"
                    "light-bowgun" -> "lb"
                    "heavy-bowgun" -> "hb"
                    "bow" -> "bow"
                    else -> "default"
                }
                "file:///android_asset/weapons/${prefix}_${rarity}.png"
            }
            "armor" -> {
                val armorType = subtype ?: "unknown"
                "file:///android_asset/armor/${armorType}_${rarity}.png"
            }
            "monsters" -> {
            val monsterId = id ?: "default"
                "file:///android_asset/monsters/$monsterId.png"
            }
            "elements" -> {
                val elementName = name ?: "default"
                "file:///android_asset/elements/$elementName.png"
            }
            else -> "file:///android_asset/default.png"
        }
    }

}