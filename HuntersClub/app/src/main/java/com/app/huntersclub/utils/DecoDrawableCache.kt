package com.app.huntersclub.utils

import android.content.Context
import android.graphics.drawable.LayerDrawable
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat

object DecoDrawableCache {
    private val cache = mutableMapOf<String, LayerDrawable>()

    fun get(key: String): LayerDrawable? = cache[key]
    fun put(key: String, drawable: LayerDrawable) { cache[key] = drawable }

    fun preloadAllDecorations(context: Context) {
        val colors = listOf(
            "violet", "lightbeige", "gray", "beige", "blue", "lime", "green",
            "darkbeige", "cyan", "yellow", "darkred", "red", "white", "gold",
            "orange", "darkblue", "darkgreen", "darkpurple"
        )

        for (slot in 1..4) {
            for (color in colors) {
                loadDecorationDrawable(context, slot, color)
            }
        }
    }

    fun loadDecorationDrawable(context: Context, slot: Int, colorName: String): LayerDrawable {
        val key = "$slot-$colorName"

        get(key)?.let { return it }

        val baseId = DecoImages.getBase(slot)
        val innerId = DecoImages.getInner(slot)

        val base = AppCompatResources.getDrawable(context, baseId)
            ?.constantState?.newDrawable()?.mutate()

        val inner = AppCompatResources.getDrawable(context, innerId)
            ?.constantState?.newDrawable()?.mutate()

        val colorRes = DecoColorMapper.getColor(context, colorName)
        val tintColor = ContextCompat.getColor(context, colorRes)
        inner?.setTint(tintColor)

        val layer = LayerDrawable(arrayOf(base, inner))

        put(key, layer)
        return layer
    }

}
