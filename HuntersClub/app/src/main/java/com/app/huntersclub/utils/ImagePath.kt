package com.app.huntersclub.utils

/**
 * Object class to manage the image path of the different
 * images that the app loads
 *
 */
object ImagePath {
    val profileAvatars = listOf(1, 2, 3, 4, 5)
    /**
     * Function to obtain the image path from assets and load them
     * We have to work with decorations so when selecting a decoration
     * changes the image to a full color decoration
     */
    fun getAssetPath(type: String, rarity: Int? = null, subtype: String? = null, id: Int? = null, name: String? = null, slot: Int? = null): String {

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
                "file:///android_asset/armor/${subtype}_${rarity}.png"
            }
            "monsters" -> {
                "file:///android_asset/monsters/$id.png"
            }
            "elements" -> {
                "file:///android_asset/elements/$name.png"
            }
            "profile" -> {
                "file:///android_asset/pfp/$id.png"
            }
            else -> "file:///android_asset/default.png"
        }
    }
}