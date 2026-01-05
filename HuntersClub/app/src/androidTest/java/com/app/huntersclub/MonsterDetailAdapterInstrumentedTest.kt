package com.app.huntersclub

import android.view.View
import android.widget.FrameLayout
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.app.huntersclub.ui.monster.MonsterDetailAdapter
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MonsterDetailAdapterInstrumentedTest {

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Test
    fun adapterShowsAltContainerWhenHasAltWeakness() {
        val weaknesses = listOf(Triple("fire", 2, 1))
        val adapter = MonsterDetailAdapter(weaknesses, hasAltWeakness = true)

        val parent = FrameLayout(context)
        val viewHolder = adapter.onCreateViewHolder(parent, 0)

        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            adapter.onBindViewHolder(viewHolder, 0)
        }

        assertEquals(View.VISIBLE, viewHolder.altContainer.visibility)
    }

    @Test
    fun adapterHidesAltContainerWhenNoAltWeakness() {
        val weaknesses = listOf(Triple("fire", 2, null))
        val adapter = MonsterDetailAdapter(weaknesses, hasAltWeakness = false)

        val parent = FrameLayout(context)
        val viewHolder = adapter.onCreateViewHolder(parent, 0)

        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            adapter.onBindViewHolder(viewHolder, 0)
        }

        assertEquals(View.GONE, viewHolder.altContainer.visibility)
    }

    @Test
    fun adapterReturnsCorrectItemCount() {
        val weaknesses = listOf(
            Triple("fire", 2, 1),
            Triple("water", 1, null),
            Triple("ice", 3, 2)
        )
        val adapter = MonsterDetailAdapter(weaknesses, hasAltWeakness = true)

        assertEquals(3, adapter.itemCount)
    }
}
