package com.app.huntersclub.data


import com.app.huntersclub.model.Armor
import com.app.huntersclub.model.Skill

class ArmorDAO(private val dbHelper: MyDatabaseHelper) {
    //SQL query to obtain all armor pieces
    fun getAllArmor(): List<Armor> {
        val db = dbHelper.openDatabase()
        val armorSkillsMap = mutableMapOf<Int, MutableList<Skill>>()
        val armorInfoMap = mutableMapOf<Int, Armor>()

        val query = """
            SELECT
                armor.id,
                armor.rarity,
                armor.rank,
                armor.armor_type,
                armor_text.name,
                armor.slot_1,
                armor.slot_2,
                armor.slot_3,
                armor.defense_base,
                armor.fire,
                armor.water,
                armor.thunder,
                armor.ice,
                armor.dragon,
                skilltree_text.name,
                armor_skill.level
            FROM armor
            JOIN armor_text
                ON armor_text.id = armor.id
            LEFT JOIN armor_skill
                ON armor_skill.armor_id = armor.id
            LEFT JOIN skilltree_text
                ON armor_skill.skilltree_id = skilltree_text.id
                AND skilltree_text.lang_id = 'es'
            WHERE
                armor_text.lang_id = 'es'
            ORDER BY
                armor.rarity, armor.armor_type, armor_text.name
        """.trimIndent()

        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(0)
                val rarity = cursor.getInt(1)
                val rank = cursor.getString(2)
                val armorType = cursor.getString(3)
                val name = cursor.getString(4)
                val slot1 = cursor.getInt(5)
                val slot2 = cursor.getInt(6)
                val slot3 = cursor.getInt(7)
                val defense = cursor.getInt(8)
                val fire = cursor.getInt(9)
                val water = cursor.getInt(10)
                val thunder = cursor.getInt(11)
                val ice = cursor.getInt(12)
                val dragon = cursor.getInt(13)
                val skillName = cursor.getString(14)
                val skillLevel = cursor.getInt(15)


                if (!armorInfoMap.containsKey(id)) {
                    armorInfoMap[id] = Armor(
                        id = id,
                        name = name,
                        imageArmor = "armor/$id.png",
                        rarity = rarity,
                        rank = rank,
                        armorType = armorType,
                        slot1 = slot1,
                        slot2 = slot2,
                        slot3 = slot3,
                        defense = defense,
                        fire = fire,
                        water = water,
                        thunder = thunder,
                        ice = ice,
                        dragon = dragon,
                        skills = emptyList()
                    )
                }

                if (skillName != null) {
                    val skill = Skill(skillName, skillLevel)
                    armorSkillsMap.getOrPut(id) { mutableListOf() }.add(skill)
                }

            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return armorInfoMap.map { (id, armor) ->
            armor.copy(skills = armorSkillsMap[id] ?: emptyList())
        }
    }

    //SQL query to get a specific piece of armor
    fun getArmorById(armorId: Int): Armor? {
        val db = dbHelper.openDatabase()
        val armorSkillsMap = mutableMapOf<Int, MutableList<Skill>>()
        val armorInfoMap = mutableMapOf<Int, Armor>()

        val query = """
        SELECT
            armor.id,
            armor.rarity,
            armor.rank,
            armor.armor_type,
            armor_text.name,
            armor.slot_1,
            armor.slot_2,
            armor.slot_3,
            armor.defense_base,
            armor.fire,
            armor.water,
            armor.thunder,
            armor.ice,
            armor.dragon,
            skilltree_text.name,
            armor_skill.level
        FROM armor
        JOIN armor_text
            ON armor_text.id = armor.id
        LEFT JOIN armor_skill
            ON armor_skill.armor_id = armor.id
        LEFT JOIN skilltree_text
            ON armor_skill.skilltree_id = skilltree_text.id
            AND skilltree_text.lang_id = 'es'
        WHERE
            armor.id = ?
            AND armor_text.lang_id = 'es'
    """.trimIndent()

        val cursor = db.rawQuery(query, arrayOf(armorId.toString()))
        //Similar armor mapping as getAllArmor()
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(0)
                val rarity = cursor.getInt(1)
                val rank = cursor.getString(2)
                val armorType = cursor.getString(3)
                val name = cursor.getString(4)
                val slot1 = cursor.getInt(5)
                val slot2 = cursor.getInt(6)
                val slot3 = cursor.getInt(7)
                val defense = cursor.getInt(8)
                val fire = cursor.getInt(9)
                val water = cursor.getInt(10)
                val thunder = cursor.getInt(11)
                val ice = cursor.getInt(12)
                val dragon = cursor.getInt(13)
                val skillName = cursor.getString(14)
                val skillLevel = cursor.getInt(15)

                if (!armorInfoMap.containsKey(id)) {
                    armorInfoMap[id] = Armor(
                        id = id,
                        name = name,
                        imageArmor = "armor/$id.png",
                        rarity = rarity,
                        rank = rank,
                        armorType = armorType,
                        slot1 = slot1,
                        slot2 = slot2,
                        slot3 = slot3,
                        defense = defense,
                        fire = fire,
                        water = water,
                        thunder = thunder,
                        ice = ice,
                        dragon = dragon,
                        skills = emptyList()
                    )
                }

                if (skillName != null) {
                    val skill = Skill(skillName, skillLevel)
                    armorSkillsMap.getOrPut(id) { mutableListOf() }.add(skill)
                }

            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        //We return the armor with the matching id required
        //Fixing the ids 2-5 and 7-10 from database
        return armorInfoMap[armorId]?.copy(skills = armorSkillsMap[armorId] ?: emptyList())
    }

}

