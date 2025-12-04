package com.app.huntersclub.data

import com.app.huntersclub.model.Charm

class CharmDAO (private val dbHelper: MyDatabaseHelper) {
    //SQL query to obtain all charms
    fun getAllCharms(): List<Charm> {
        val db = dbHelper.openDatabase()
        val charms = mutableListOf<Charm>()

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

                val charm = Charm(
                    id = id,
                    name = name,
                    imageCharm = "charms/$id.png",
                    rarity = rarity,
                    skillTreeName = skillTreeName,
                    skillLevel = skillLevel
                )

                charms.add(charm)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return charms
    }
    //SQL query to get a specific charm
    fun getCharmById(charmId: Int): Charm? {
        val db = dbHelper.openDatabase()

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
        LIMIT 1
    """.trimIndent()

        val cursor = db.rawQuery(query, arrayOf(charmId.toString()))

        var charm: Charm? = null
        if (cursor.moveToFirst()) {
            val id = cursor.getInt(0)
            val rarity = cursor.getInt(1)
            val skillLevel = cursor.getInt(2)
            val name = cursor.getString(3)
            val skillTreeName = cursor.getString(4)

            charm = Charm(
                id = id,
                name = name,
                imageCharm = "charms/$id.png",
                rarity = rarity,
                skillTreeName = skillTreeName,
                skillLevel = skillLevel
            )
        }

        cursor.close()
        db.close()

        return charm
    }
}