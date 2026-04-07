package com.app.huntersclub.data.dao

import com.app.huntersclub.data.database.MyDatabaseHelper
import com.app.huntersclub.model.Decoration
import com.app.huntersclub.model.Skill

/**
 * Data Access Object Class to retrieve decoration data
 * with its skills from the Monster Hunter: World database
 *
 */

class DecoDAO(private val dbHelper: MyDatabaseHelper) {
    /**
     * SQL query to obtain all decorations
     *
     */
    fun getAllDecorations(): List<Decoration> {
        val db = dbHelper.openDatabase()

        val query = """
        SELECT
            decoration.id,
            decoration.slot,
            decoration.rarity,
            decoration.skilltree_level,
            decoration.skilltree2_level,
            decoration_text.name,
            skilltree_text_first.name,
            skilltree_text_second.name,
            decoration.icon_color
        FROM decoration
        JOIN decoration_text
            ON decoration_text.id = decoration.id
            AND decoration_text.lang_id = 'es'
        JOIN skilltree_text AS skilltree_text_first
            ON decoration.skilltree_id = skilltree_text_first.id
            AND skilltree_text_first.lang_id = 'es'
        LEFT JOIN skilltree_text AS skilltree_text_second
            ON decoration.skilltree2_id = skilltree_text_second.id
            AND skilltree_text_second.lang_id = 'es'
        ORDER BY decoration.slot, decoration_text.name;
    """.trimIndent()

        val cursor = db.rawQuery(query, null)
        val decorations = mutableListOf<Decoration>()

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(0)
                val slot = cursor.getInt(1)
                val rarity = cursor.getInt(2)
                val skillLevel1 = cursor.getInt(3)
                val skillLevel2 = cursor.getInt(4)
                val decorationName = cursor.getString(5)
                val skillName1 = cursor.getString(6)
                val skillName2 = cursor.getString(7)
                val colour = cursor.getString(8)


                val skills = mutableListOf<Skill>()
                skills.add(Skill(skillName1, skillLevel1))
                if (skillName2 != null) {
                    skills.add(Skill(skillName2, skillLevel2))
                }

                decorations.add(
                    Decoration(
                        id = id,
                        name = decorationName,
                        imageDeco = "decorations/$id.png",
                        slot = slot,
                        rarity = rarity,
                        skills = skills,
                        colour = colour
                    )
                )
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return decorations
    }
    /**
     * SQL query to get a specific decoration
     * We keep the method in case we ONLY need to load an individual
     * decoration. For repetitive or massive loads, memory cache is the way
     *
     */
    fun getDecorationById(decorationId: Int): Decoration? {
        val db = dbHelper.openDatabase()

        val query = """
        SELECT
            decoration.id, 
            decoration.slot,
            decoration.rarity,
            decoration.skilltree_level,
            decoration.skilltree2_level,
            decoration_text.name,
            skilltree_text_first.name,
            skilltree_text_second.name,
            decoration.icon_color
        FROM decoration
        JOIN decoration_text 
            ON decoration.id = decoration_text.id
            AND decoration_text.lang_id = 'es'
        JOIN skilltree_text AS skilltree_text_first
            ON decoration.skilltree_id = skilltree_text_first.id
            AND skilltree_text_first.lang_id = 'es'
        LEFT JOIN skilltree_text AS skilltree_text_second
            ON decoration.skilltree2_id = skilltree_text_second.id
            AND skilltree_text_second.lang_id = 'es'
        WHERE decoration.id = ?
    """.trimIndent()

        val cursor = db.rawQuery(query, arrayOf(decorationId.toString()))

        var decoration: Decoration? = null

        if (cursor.moveToFirst()) {

            val id = cursor.getInt(0)
            val slot = cursor.getInt(1)
            val rarity = cursor.getInt(2)
            val skillLevel1 = cursor.getInt(3)
            val skillLevel2 = cursor.getInt(4)
            val decorationName = cursor.getString(5)
            val skillName1 = cursor.getString(6)
            val skillName2 = cursor.getString(7)
            val colour = cursor.getString(8)

            val skills = mutableListOf<Skill>()
            skills.add(Skill(skillName1, skillLevel1))

            if (skillName2 != null) {
                skills.add(Skill(skillName2, skillLevel2))
            }

            decoration = Decoration(
                id = id,
                name = decorationName,
                imageDeco = "decorations/$id.png",
                slot = slot,
                rarity = rarity,
                skills = skills,
                colour = colour
            )
        }

        cursor.close()
        db.close()

        return decoration
    }
}