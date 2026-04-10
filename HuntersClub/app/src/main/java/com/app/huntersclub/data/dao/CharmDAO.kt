package com.app.huntersclub.data.dao

import com.app.huntersclub.data.database.MyDatabaseHelper
import com.app.huntersclub.model.Charm
import com.app.huntersclub.model.Skill

/**
 * Data Access Object Class to retrieve charm data
 * with its skills from the Monster Hunter: World database
 *
 */

class CharmDAO(private val dbHelper: MyDatabaseHelper) {
    /**
     * SQL query to obtain all charms
     * Some charms have more than 1 skill, if we want to remove
     * The "duplicated" charms we have to map them
     *
     */

    fun getAllCharms(): List<Charm> {
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
            WHERE skilltree_text.lang_id = 'es'
              AND charm_text.lang_id = 'es'
            ORDER BY charm.id, charm_text.name
        """.trimIndent()

        val cursor = db.rawQuery(query, null)
        val charms = mutableMapOf<Int, Charm>()

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(0)
                val rarity = cursor.getInt(1)
                val skillLevel = cursor.getInt(2)
                val name = cursor.getString(3)
                val skillTreeName = cursor.getString(4)

                val newSkill = Skill(skillTreeName, skillLevel)

                val updatedCharm = charms[id]?.let { existing ->
                    existing.copy(
                        skills = existing.skills + newSkill
                    )
                } ?: Charm(
                    id = id,
                    name = name,
                    imageCharm = "charms/$id.png",
                    rarity = rarity,
                    skills = listOf(newSkill)
                )

                charms[id] = updatedCharm

            } while (cursor.moveToNext()) }

        cursor.close()
        db.close()

        return charms.values.toList()
    }
}
