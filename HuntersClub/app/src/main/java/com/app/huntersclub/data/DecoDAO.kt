package com.app.huntersclub.data

import com.app.huntersclub.model.Decoration

class DecoDAO (private val dbHelper: MyDatabaseHelper) {
    //SQL query to obtain all decorations
    fun getAllDecorations(): List<Decoration> {
        val db = dbHelper.openDatabase()
        val decorations = mutableListOf<Decoration>()

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

                val decoration = Decoration(
                    id = id,
                    name = name,
                    imageDeco = "decorations/$id.png",
                    slot = slot,
                    rarity = rarity,
                    skillTreeName = skillTreeName,
                    skillTreeLevel = skillTreeLevel
                )

                decorations.add(decoration)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return decorations
    }
}