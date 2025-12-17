package com.app.huntersclub.data

import com.app.huntersclub.model.Weapon

class WeaponDAO(private val dbHelper: MyDatabaseHelper) {
    //SQL query to get a list of all weapons
    fun getAllWeapons(): List<Weapon> {
        val db = dbHelper.openDatabase()
        val weapons = mutableListOf<Weapon>()

        val query = """
            SELECT
                weapon.id, 
                weapon.weapon_type,
                weapon.rarity,
                weapon_text.name,
                weapon.attack,
                weapon.affinity,
                weapon.defense,
                weapon.slot_1,
                weapon.slot_2,
                weapon.slot_3,
                weapon.element_hidden,
                weapon.element1,
                weapon.element1_attack,
                weapon.element2,
                weapon.element2_attack,
                weapon.elderseal,
                weapon.sharpness,
                weapon.armorset_bonus_id,
                skilltree_text.name,
                weapon_skill.level
            FROM weapon
            JOIN weapon_text
                ON weapon.id = weapon_text.id
                AND weapon_text.lang_id = 'es'
            LEFT JOIN weapon_skill
                ON weapon_skill.weapon_id = weapon.id
            LEFT JOIN skilltree_text
                ON weapon_skill.skilltree_id = skilltree_text.id
                AND skilltree_text.lang_id = 'es'
            ORDER BY weapon.weapon_type, weapon.rarity, weapon_text.name
        """.trimIndent()

        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(0)
                val name = cursor.getString(3)
                val rarity = cursor.getInt(2)
                val weaponType = cursor.getString(1)
                val atk = cursor.getInt(4)
                val affinity = cursor.getInt(5)
                val def = cursor.getInt(6)
                val slot1 = cursor.getInt(7)
                val slot2 = cursor.getInt(8)
                val slot3 = cursor.getInt(9)
                val elementHidden = cursor.getInt(10) == 1
                val element1 = cursor.getString(11)
                val element1Dmg = if (cursor.isNull(12)) null else cursor.getInt(12)
                val element2 = cursor.getString(13)
                val element2Dmg = if (cursor.isNull(14)) null else cursor.getInt(14)
                val elderSeal = cursor.getString(15)
                val sharpness = if (cursor.isNull(16)) null else cursor.getString(16)
                val armorSetBonusId = if (cursor.isNull(17)) null else cursor.getInt(17)
                val skillName = cursor.getString(18)
                val skillLevel = if (cursor.isNull(19)) null else cursor.getInt(19)

                val weapon = Weapon(
                    id = id,
                    name = name,
                    imageWeapon = "weapons/$id.png",
                    rarity = rarity,
                    weaponType = weaponType,
                    atk = atk,
                    affinity = affinity,
                    def = def,
                    slot1 = slot1,
                    slot2 = slot2,
                    slot3 = slot3,
                    elementHidden = elementHidden,
                    element1 = element1,
                    element1Dmg = element1Dmg,
                    element2 = element2,
                    element2Dmg = element2Dmg,
                    elderSeal = elderSeal,
                    sharpness = sharpness?.split(",")?.map { it.toInt() },
                    armorSetBonusId = armorSetBonusId,
                    skillName = skillName,
                    skillLevel = skillLevel
                )
                weapons.add(weapon)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return weapons
    }
    //SQL query to get a specific weapon
    fun getWeaponById(weaponId: Int): Weapon? {
        val db = dbHelper.openDatabase()

        val query = """
        SELECT
            weapon.id, 
            weapon.weapon_type,
            weapon.rarity,
            weapon_text.name,
            weapon.attack,
            weapon.affinity,
            weapon.defense,
            weapon.slot_1,
            weapon.slot_2,
            weapon.slot_3,
            weapon.element_hidden,
            weapon.element1,
            weapon.element1_attack,
            weapon.element2,
            weapon.element2_attack,
            weapon.elderseal,
            weapon.sharpness,
            weapon.armorset_bonus_id,
            skilltree_text.name,
            weapon_skill.level
        FROM weapon
        JOIN weapon_text
            ON weapon.id = weapon_text.id
            AND weapon_text.lang_id = 'es'
        LEFT JOIN weapon_skill
            ON weapon_skill.weapon_id = weapon.id
        LEFT JOIN skilltree_text
            ON weapon_skill.skilltree_id = skilltree_text.id
            AND skilltree_text.lang_id = 'es'
        WHERE weapon.id = ?
        LIMIT 1
    """.trimIndent()

        val cursor = db.rawQuery(query, arrayOf(weaponId.toString()))

        var weapon: Weapon? = null
        if (cursor.moveToFirst()) {
            val id = cursor.getInt(0)
            val name = cursor.getString(3)
            val rarity = cursor.getInt(2)
            val weaponType = cursor.getString(1)
            val atk = cursor.getInt(4)
            val affinity = cursor.getInt(5)
            val def = cursor.getInt(6)
            val slot1 = cursor.getInt(7)
            val slot2 = cursor.getInt(8)
            val slot3 = cursor.getInt(9)
            val elementHidden = cursor.getInt(10) == 1
            val element1 = cursor.getString(11)
            val element1Dmg = if (cursor.isNull(12)) null else cursor.getInt(12)
            val element2 = cursor.getString(13)
            val element2Dmg = if (cursor.isNull(14)) null else cursor.getInt(14)
            val elderSeal = cursor.getString(15)
            val sharpness = if (cursor.isNull(16)) null else cursor.getString(16)
            val armorSetBonusId = if (cursor.isNull(17)) null else cursor.getInt(17)
            val skillName = cursor.getString(18)
            val skillLevel = if (cursor.isNull(19)) null else cursor.getInt(19)

            weapon = Weapon(
                id = id,
                name = name,
                imageWeapon = "weapons/$id.png",
                rarity = rarity,
                weaponType = weaponType,
                atk = atk,
                affinity = affinity,
                def = def,
                slot1 = slot1,
                slot2 = slot2,
                slot3 = slot3,
                elementHidden = elementHidden,
                element1 = element1,
                element1Dmg = element1Dmg,
                element2 = element2,
                element2Dmg = element2Dmg,
                elderSeal = elderSeal,
                sharpness = sharpness?.split(",")?.map { it.toInt() },
                armorSetBonusId = armorSetBonusId,
                skillName = skillName,
                skillLevel = skillLevel
            )
        }

        cursor.close()
        db.close()

        return weapon
    }
}
