package com.app.huntersclub.data

import com.app.huntersclub.model.Decoration
import com.app.huntersclub.model.Skill

class DecoDAO(private val dbHelper: MyDatabaseHelper) {
    //SQL query to obtain all decorations
    fun getAllDecorations(): List<Decoration> {
        val db = dbHelper.openDatabase()

        val decorationsMap = mutableMapOf<Int, MutableList<Skill>>()
        val decoInfoMap = mutableMapOf<Int, Triple<String, Int, Int>>()

        val query = """
            SELECT
                decoration.id, 
                decoration.slot,
                decoration.rarity,
                decoration.skilltree_level,
                decoration_text.name,
                skilltree_text.name
            FROM decoration
            JOIN skilltree_text 
                ON decoration.skilltree_id = skilltree_text.id
            JOIN decoration_text 
                ON decoration.id = decoration_text.id
            WHERE skilltree_text.lang_id = 'es'
              AND decoration_text.lang_id = 'es'
            ORDER BY decoration.slot, decoration_text.name
        """.trimIndent()

        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(0)
                val slot = cursor.getInt(1)
                val rarity = cursor.getInt(2)
                val skillTreeLevel = cursor.getInt(3)
                val name = cursor.getString(4)
                val skillTreeName = cursor.getString(5)

                if (!decoInfoMap.containsKey(id)) {
                    decoInfoMap[id] = Triple(name, rarity, slot)
                }

                val skill = Skill(skillTreeName, skillTreeLevel)
                decorationsMap.getOrPut(id) { mutableListOf() }.add(skill)

            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return decoInfoMap.map { (id, info) ->
            val (name, rarity, slot) = info
            Decoration(
                id = id,
                name = name,
                imageDeco = "decorations/$id.png",
                slot = slot,
                rarity = rarity,
                skills = decorationsMap[id] ?: emptyList()
            )
        }
    }
    //SQL query to get a specific decoration
    fun getDecorationById(decorationId: Int): Decoration? {
        val db = dbHelper.openDatabase()

        val query = """
        SELECT
            decoration.id, 
            decoration.slot,
            decoration.rarity,
            decoration.skilltree_level,
            decoration_text.name,
            skilltree_text.name
        FROM decoration
        JOIN skilltree_text 
            ON decoration.skilltree_id = skilltree_text.id
        JOIN decoration_text 
            ON decoration.id = decoration_text.id
        WHERE decoration.id = ?
          AND skilltree_text.lang_id = 'es'
          AND decoration_text.lang_id = 'es'
    """.trimIndent()

        val cursor = db.rawQuery(query, arrayOf(decorationId.toString()))

        var decoration: Decoration? = null
        val skills = mutableListOf<Skill>()

        if (cursor.moveToFirst()) {
            val id = cursor.getInt(0)
            val slot = cursor.getInt(1)
            val rarity = cursor.getInt(2)
            val name = cursor.getString(4)

            do {
                val skillTreeLevel = cursor.getInt(3)
                val skillTreeName = cursor.getString(5)
                skills.add(Skill(skillTreeName, skillTreeLevel))
            } while (cursor.moveToNext())

            decoration = Decoration(
                id = id,
                name = name,
                imageDeco = "decorations/$id.png",
                slot = slot,
                rarity = rarity,
                skills = skills
            )
        }

        cursor.close()
        db.close()

        return decoration
    }
}