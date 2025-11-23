package com.app.huntersclub.ui.home

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.huntersclub.R

class WeaknessAdapter(private val weaknesses: List<Triple<String, Int, Int>>) :
    RecyclerView.Adapter<WeaknessAdapter.WeaknessViewHolder>() {

    inner class WeaknessViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val icon: ImageView = itemView.findViewById(R.id.weaknessIcon)
        val star1: ImageView = itemView.findViewById(R.id.star1)
        val star2: ImageView = itemView.findViewById(R.id.star2)
        val star3: ImageView = itemView.findViewById(R.id.star3)
        val altStar1: ImageView = itemView.findViewById(R.id.altStar1)
        val altStar2: ImageView = itemView.findViewById(R.id.altStar2)
        val altStar3: ImageView = itemView.findViewById(R.id.altStar3)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeaknessViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_weakness, parent, false)
        return WeaknessViewHolder(view)
    }

    override fun onBindViewHolder(holder: WeaknessViewHolder, position: Int) {
        val (iconName, normalValue, altValue) = weaknesses[position]

        //Loading element icon
        try {
            val inputStream = holder.itemView.context.assets.open("elements/$iconName.png")
            val drawable = Drawable.createFromStream(inputStream, null)
            holder.icon.setImageDrawable(drawable)
            inputStream.close()
        } catch (e: Exception) {
            holder.icon.setImageResource(android.R.drawable.ic_menu_report_image)
        }
        //Loading stars
        val context = holder.itemView.context
        val starOn = Drawable.createFromStream(context.assets.open("elements/star.png"), null)
        val starOff = Drawable.createFromStream(context.assets.open("elements/staroff.png"), null)

        //Stars for monster weaknesses
        val stars = listOf(holder.star1, holder.star2, holder.star3)
        for (i in stars.indices) {
            if (i < normalValue) stars[i].setImageDrawable(starOn)
            else stars[i].setImageDrawable(starOff)
        }

        //Alternative stars for weaknesses
        val altStars = listOf(holder.altStar1, holder.altStar2, holder.altStar3)
        for (i in altStars.indices) {
            if (i < altValue) altStars[i].setImageDrawable(starOn)
            else altStars[i].setImageDrawable(starOff)
        }
    }

    override fun getItemCount() = weaknesses.size
}



