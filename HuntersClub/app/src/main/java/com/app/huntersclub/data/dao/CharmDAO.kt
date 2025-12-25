package com.app.huntersclub.data.dao

import com.app.huntersclub.data.database.MyDatabaseHelper
import com.app.huntersclub.model.Charm
import com.app.huntersclub.model.Skill

class CharmDAO(private val dbHelper: MyDatabaseHelper) {
    //SQL query to obtain all charms
    //Some charms have more than 1 skill, if we want to remove
    //The "duplicated" charms we have to map them
    fun getAllCharms(): List<Charm> {
        val db = dbHelper.openDatabase()
        val charmsMap = mutableMapOf<Int, MutableList<Skill>>()
        val charmInfoMap = mutableMapOf<Int, Pair<String, Int>>()

        val query = """
            SELECT
                charm.id, 
                charm.rarity,
                charm_skill.level,
                charm_text.name,
                skilltree_text.name
            FROM charm
            JOIN charm_skill 
                ON charm.id = charm_skill.charm_id
            JOIN skilltree_text
                ON charm_skill.skilltree_id = skilltree_text.id
            JOIN charm_text
                ON charm_text.id = charm.id
            WHERE skilltree_text.lang_id = 'es'
              AND charm_text.lang_id = 'es'
            ORDER BY charm.id, charm_text.name
        """.trimIndent()

        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(0)
                val rarity = cursor.getInt(1)
                val skillLevel = cursor.getInt(2)
                val name = cursor.getString(3)
                val skillTreeName = cursor.getString(4)

                charmInfoMap[id] = Pair(name, rarity)

                val skill = Skill(skillTreeName, skillLevel)
                charmsMap.getOrPut(id) { mutableListOf() }.add(skill)

            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return charmsMap.map { (id, skills) ->
            val (name, rarity) = charmInfoMap[id]!!
            Charm(
                id = id,
                name = name,
                imageCharm = "charms/$id.png",
                rarity = rarity,
                skills = skills
            )
        }
    }

    //SQL query to get a specific charm
    //We keep the method in case we ONLY need to load an individual
    //charm. For repetitive or massive loads, memory cache is the way
    fun getCharmById(charmId: Int): Charm? {
        val db = dbHelper.openDatabase()
        val skills = mutableListOf<Skill>()
        var charmName: String?
        var charmRarity: Int?

        val query = """
            SELECT
                charm.id, 
                charm.rarity,
                charm_skill.level,
                charm_text.name,
                skilltree_text.name
            FROM charm
            JOIN charm_skill 
                ON charm.id = charm_skill.charm_id
            JOIN skilltree_text
                ON charm_skill.skilltree_id = skilltree_text.id
            JOIN charm_text
                ON charm_text.id = charm.id
            WHERE charm.id = ?
              AND skilltree_text.lang_id = 'es'
              AND charm_text.lang_id = 'es'
        """.trimIndent()

        val cursor = db.rawQuery(query, arrayOf(charmId.toString()))

        var charm: Charm? = null
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(0)
                val rarity = cursor.getInt(1)
                val skillLevel = cursor.getInt(2)
                val name = cursor.getString(3)
                val skillTreeName = cursor.getString(4)

                charmName = name
                charmRarity = rarity
                skills.add(Skill(skillTreeName, skillLevel))

                charm = Charm(
                    id = id,
                    name = charmName,
                    imageCharm = "charms/$id.png",
                    rarity = charmRarity,
                    skills = skills
                )
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return charm
    }
}
