package com.app.huntersclub.ui.monster

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.app.huntersclub.R
import com.app.huntersclub.utils.ImagePath
import com.bumptech.glide.Glide


class MonsterDetailAdapter(
    private val weaknesses: List<Triple<String, Int, Int?>>,
    private val hasAltWeakness: Boolean
) : RecyclerView.Adapter<MonsterDetailAdapter.WeaknessViewHolder>() {

    inner class WeaknessViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val icon: ImageView = itemView.findViewById(R.id.weaknessIcon)
        val star1: ImageView = itemView.findViewById(R.id.star1)
        val star2: ImageView = itemView.findViewById(R.id.star2)
        val star3: ImageView = itemView.findViewById(R.id.star3)
        val altStar1: ImageView = itemView.findViewById(R.id.altStar1)
        val altStar2: ImageView = itemView.findViewById(R.id.altStar2)
        val altStar3: ImageView = itemView.findViewById(R.id.altStar3)
        val altContainer: View = itemView.findViewById(R.id.altContainer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeaknessViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_weakness, parent, false)
        return WeaknessViewHolder(view)
    }

    override fun onBindViewHolder(holder: WeaknessViewHolder, position: Int) {
        val (iconName, normalValue, altValue) = weaknesses[position]

        //Load image from app\src\main\assets\monsters using Glide to obtain better performance
        //Using the standard loading from assets has a heavy impact on performance
        //By using Glide, performance increases and we don't have small stutters/lag on application
        val path = ImagePath.getAssetPath("elements", name = iconName)
        Glide.with(holder.itemView.context)
            .load(path)
            .into(holder.icon)

        val starOn = ImagePath.getAssetPath("elements", name = "star")
        val starOff = ImagePath.getAssetPath("elements", name = "staroff")

        //Stars for monster weaknesses
        val stars = listOf(holder.star1, holder.star2, holder.star3)
        for (i in stars.indices) {
            val starPath = if (i < normalValue) starOn else starOff
            Glide.with(holder.itemView.context)
                .load(starPath)
                .into(stars[i])
        }

        //Alternative stars for weaknesses
        if (hasAltWeakness && altValue != null) {
            holder.altContainer.visibility = View.VISIBLE
            val altStars = listOf(holder.altStar1, holder.altStar2, holder.altStar3)
            for (i in altStars.indices) {
                val starPath = if (i < altValue) starOn else starOff
                Glide.with(holder.itemView.context)
                    .load(starPath)
                    .into(altStars[i])
            }
        } else {
            holder.altContainer.visibility = View.GONE
        }
    }

    override fun getItemCount() = weaknesses.size
}
