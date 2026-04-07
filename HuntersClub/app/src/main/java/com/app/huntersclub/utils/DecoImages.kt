package com.app.huntersclub.utils

import com.app.huntersclub.R

object DecoImages {

    private val baseMap = mapOf(
        1 to R.drawable.deco_1_base,
        2 to R.drawable.deco_2_base,
        3 to R.drawable.deco_3_base,
        4 to R.drawable.deco_4_base
    )

    private val innerMap = mapOf(
        1 to R.drawable.deco_1_inner,
        2 to R.drawable.deco_2_inner,
        3 to R.drawable.deco_3_inner,
        4 to R.drawable.deco_4_inner
    )

    fun getBase(slot: Int): Int = baseMap[slot] ?: R.drawable.deco_1_base
    fun getInner(slot: Int): Int = innerMap[slot] ?: R.drawable.deco_1_inner
}

