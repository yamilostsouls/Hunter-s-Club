package com.app.huntersclub.utils

import android.content.Context
import com.app.huntersclub.R

object DecoColorMapper {

    fun getColor(context: Context, name: String): Int {
        return when (name.lowercase()) {
            "violet" -> R.color.violet
            "lightbeige" -> R.color.light_beige
            "gray" -> R.color.gray
            "beige" -> R.color.beige
            "blue" -> R.color.blue2
            "lime" -> R.color.lime
            "green" -> R.color.green
            "darkbeige" -> R.color.dark_beige
            "cyan" -> R.color.cyan
            "yellow" -> R.color.yellow
            "darkred" -> R.color.dark_red
            "red" -> R.color.red
            "white" -> R.color.white
            "gold" -> R.color.gold
            "orange" -> R.color.orange
            "darkblue" -> R.color.dark_blue2
            "darkgreen" -> R.color.dark_green
            "darkpurple" -> R.color.dark_purple

            else -> R.color.black
        }
    }
}
