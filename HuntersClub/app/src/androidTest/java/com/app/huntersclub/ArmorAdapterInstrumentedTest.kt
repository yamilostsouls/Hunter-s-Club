package com.app.huntersclub

import android.widget.Filter
import android.widget.FrameLayout
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.app.huntersclub.model.Armor
import com.app.huntersclub.model.Skill
import com.app.huntersclub.ui.gallery.ArmorAdapter
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ArmorAdapterInstrumentedTest {

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    private val armors = listOf(
        Armor(
            id = 1,
            name = "Cota de cuero",
            imageArmor = "chest_1.png",
            rarity = 1,
            rank = "LR",
            armorType = "chest",
            slot1 = 0,
            slot2 = 0,
            slot3 = 0,
            defense = 2,
            fire = 2,
            water = 0,
            thunder = 0,
            ice = 0,
            dragon = 0,
            skills = emptyList()
        ),
        Armor(
            id = 2,
            name = "Yelmo de Vangis α",
            imageArmor = "head_7.png",
            rarity = 7,
            rank = "HR",
            armorType = "head",
            slot1 = 1,
            slot2 = 0,
            slot3 = 0,
            defense = 58,
            fire = 1,
            water = 1,
            thunder = -3,
            ice = 2,
            dragon = -3,
            skills = listOf(Skill("Artesanía",1), Skill("Bonus rompe-partes", 1))
        ),
        Armor(
            id = 3,
            name = "Escama de Escadora β+",
            imageArmor = "head_12.png",
            rarity = 12,
            rank = "MR",
            armorType = "legs",
            slot1 = 4,
            slot2 = 4,
            slot3 = 2,
            defense = 160,
            fire = 3,
            water = 0,
            thunder = 0,
            ice = 3,
            dragon = 2,
            skills = listOf(Skill("Bonus defensa",3))
        )
    )

    @Test
    fun setDataLoadsFullList() {
        val adapter = ArmorAdapter {}
        adapter.setData(armors)

        assertEquals(3, adapter.itemCount)
        assertEquals("Cota de cuero", adapter.currentList[0].name)
    }

    @Test
    fun filterEmptyQueryReturnsAllArmors() {
        val adapter = ArmorAdapter {}
        adapter.setData(armors)

        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            adapter.filter.filter("", Filter.FilterListener {
                assertEquals(3, adapter.itemCount)
            })
        }
    }

    @Test
    fun filterPartialNameReturnsMatchingArmor() {
        val adapter = ArmorAdapter {}
        adapter.setData(armors)

        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            adapter.filter.filter("escadora", Filter.FilterListener {
                assertEquals(1, adapter.itemCount)
                assertTrue(adapter.currentList.all { it.name.lowercase().contains("escadora") })
            })
        }
    }

    @Test
    fun filterWithNoMatchesReturnsEmptyList() {
        val adapter = ArmorAdapter {}
        adapter.setData(armors)

        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            adapter.filter.filter("Torso cresta Safi β+", Filter.FilterListener {
                assertEquals(0, adapter.itemCount)
            })
        }
    }

    @Test
    fun clickListenerIsInvokedWithCorrectArmor() {
        var clickedArmor: Armor? = null
        val adapter = ArmorAdapter { armor -> clickedArmor = armor }
        adapter.setData(listOf(armors[1]))

        val parent = FrameLayout(context)
        val viewHolder = adapter.onCreateViewHolder(parent, 0)

        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            adapter.onBindViewHolder(viewHolder, 0)
            viewHolder.itemView.performClick()
        }

        assertNotNull(clickedArmor)
        assertEquals("Yelmo de Vangis α", clickedArmor?.name)
    }
}
