package com.app.huntersclub.data

import com.app.huntersclub.model.Monster
import com.app.huntersclub.model.MonsterData

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
        //List structure for monsters
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(0)
                val name = cursor.getString(1)
                val monClass = cursor.getString(2)
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

    fun getMonsterById(monsterId: Int): MonsterData? {

        val db = dbHelper.openDatabase()
        val query = """
            SELECT 
                monster.id,
                monster_text.name, 
                monster_text.ecology,
                monster_text.description,
                monster.weakness_fire,
                monster.weakness_water,
                monster.weakness_thunder,
                monster.weakness_ice,
                monster.weakness_dragon,
                monster.weakness_poison,
                monster.weakness_sleep,
                monster.weakness_paralysis,
                monster.weakness_blast,
                monster.weakness_stun,
                monster.alt_weakness_fire,
                monster.alt_weakness_water,
                monster.alt_weakness_thunder,
                monster.alt_weakness_ice,
                monster.alt_weakness_dragon,
                monster.alt_weakness_poison,
                monster.alt_weakness_sleep,
                monster.alt_weakness_paralysis,
                monster.alt_weakness_blast,
                monster.alt_weakness_stun
            FROM monster_text
            JOIN monster 
                ON monster.id = monster_text.id
            WHERE 
                monster.size = 'large'
                AND monster_text.lang_id = 'es'
                AND monster.id = ?
        """.trimIndent()

        val cursor = db.rawQuery(query, arrayOf(monsterId.toString()))
        var monster: MonsterData? = null
        //Monster page with the needed data
        if (cursor.moveToFirst()) {
            monster = MonsterData(
                id = cursor.getInt(0),
                name = cursor.getString(1),
                monCategory = cursor.getString(2),
                description = cursor.getString(3),
                imagenResId = "monsters/$monsterId.png",
                weaknessFire = cursor.getInt(4),
                weaknessWater = cursor.getInt(5),
                weaknessThunder = cursor.getInt(6),
                weaknessIce = cursor.getInt(7),
                weaknessDragon = cursor.getInt(8),
                weaknessPoison = cursor.getInt(9),
                weaknessSleep = cursor.getInt(10),
                weaknessParalysis = cursor.getInt(11),
                weaknessBlast = cursor.getInt(12),
                weaknessStun = cursor.getInt(13),
                altWeaknessFire = cursor.getInt(14),
                altWeaknessWater = cursor.getInt(15),
                altWeaknessThunder = cursor.getInt(16),
                altWeaknessIce = cursor.getInt(17),
                altWeaknessDragon = cursor.getInt(18),
                altWeaknessPoison = cursor.getInt(19),
                altWeaknessSleep = cursor.getInt(20),
                altWeaknessParalysis = cursor.getInt(21),
                altWeaknessBlast = cursor.getInt(22),
                altWeaknessStun = cursor.getInt(23))
        }

        cursor.close()
        db.close()

        return monster

    }
}
