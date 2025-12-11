package com.app.huntersclub

import android.widget.Filter
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.app.huntersclub.model.Monster
import com.app.huntersclub.ui.home.MonsterAdapter
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MonsterAdapterInstrumentedTest {

    private val monsters = listOf(
        Monster(1, "Alatreon", "Dragón anciano", "144.png"),
        Monster(2, "Dodogama", "Wyvern de colmillos", "35.png"),
        Monster(3, "Rajang", "Bestia de colmillos", "138.png")
    )

    @Test
    fun setDataLoadsFullList() {
        val adapter = MonsterAdapter()
        adapter.setData(monsters)

        assertEquals(3, adapter.itemCount)
        assertEquals("Alatreon", adapter.currentList[0].name)
    }

    @Test
    fun filterEmptyQueryReturnsAllMonsters() {
        val adapter = MonsterAdapter()
        adapter.setData(monsters)

        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            adapter.filter.filter("", Filter.FilterListener {
                assertEquals(3, adapter.itemCount)
            })
        }
    }

    @Test
    fun filterPartialNameReturnsMatchingMonster() {
        val adapter = MonsterAdapter()
        adapter.setData(monsters)

        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            adapter.filter.filter("ala", Filter.FilterListener {
                assertEquals(1, adapter.itemCount)
                assertEquals("Alatreon", adapter.currentList[0].name)
            })
        }
    }

    @Test
    fun filterWithNoMatchesReturnsEmptyList() {
        val adapter = MonsterAdapter()
        adapter.setData(monsters)

        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            adapter.filter.filter("phoenix", Filter.FilterListener {
                assertEquals(0, adapter.itemCount)
            })
        }
    }

    @Test
    fun clickListenerIsInvokedWithCorrectMonster() {
        var clickedMonster: Monster? = null
        val adapter = MonsterAdapter { monster ->
            clickedMonster = monster
        }

        adapter.setData(listOf(monsters[1]))
        adapter.onItemClickListener?.invoke(monsters[1])

        assertNotNull(clickedMonster)
        assertEquals("Dodogama", clickedMonster?.name)
    }
}
