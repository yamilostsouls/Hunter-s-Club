package com.app.huntersclub

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.app.huntersclub.model.Monster
import com.app.huntersclub.ui.monster.MonsterAdapter
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

        assertEquals(3, adapter.currentList.size)
        assertEquals("Alatreon", adapter.currentList[0].name)
    }

    @Test
    fun filterEmptyQueryReturnsAllMonsters() {
        val adapter = MonsterAdapter()
        adapter.setData(monsters)

        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            adapter.filter.filter("")
        }
        InstrumentationRegistry.getInstrumentation().waitForIdleSync()
        assertEquals(3, adapter.currentList.size)
    }

    @Test
    fun filterWithNoMatchesReturnsEmptyList() {
        val adapter = MonsterAdapter()
        adapter.setData(monsters)

        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            adapter.filter.filter("Fatalis")
        }

        var attempts = 0
        while (adapter.currentList.size != 0 && attempts < 10) {
            Thread.sleep(50)
            attempts++
        }

        assertEquals(0, adapter.currentList.size)
    }

    @Test
    fun filterPartialNameReturnsMatchingMonster() {
        val adapter = MonsterAdapter()
        adapter.setData(monsters)

        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            adapter.filter.filter("ala")
        }

        var attempts = 0
        while (adapter.currentList.size != 1 && attempts < 10) {
            Thread.sleep(50)
            attempts++
        }

        assertEquals(1, adapter.currentList.size)
        assertEquals("Alatreon", adapter.currentList[0].name)
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
