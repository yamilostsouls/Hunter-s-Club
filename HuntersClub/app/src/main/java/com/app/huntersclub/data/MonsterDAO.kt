package com.app.huntersclub.data

import com.app.huntersclub.model.Monster

class MonsterDAO(private val dbHelper: MyDatabaseHelper) {

    fun getAllMonsters(): List<Monster> {
        val list = mutableListOf<Monster>()
        val db = dbHelper.openDatabase()

        val cursor = db.rawQuery(
            """
            SELECT 
                monster_text.id,
                monster_text.name, 
                monster_text.ecology
            FROM monster_text
            JOIN monster
                ON monster.id = monster_text.id
            WHERE 
                monster.size = 'large'
                AND monster_text.lang_id = 'es'
            ORDER BY
                monster_text.name;
            """.trimIndent(),
            null
        )

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(0)          // id
                val name = cursor.getString(1)     // name
                val monClass = cursor.getString(2) // ecology
                //Image load
                list.add(
                    Monster(
                        id = id,
                        name = name,
                        monCategory = monClass,
                        imagenResId = "monsters/$id.png"
                    )
                )
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return list
    }
}
