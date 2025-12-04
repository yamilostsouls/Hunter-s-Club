package com.app.huntersclub.ui.gallery

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.huntersclub.R
import com.app.huntersclub.model.Armor
import com.bumptech.glide.Glide

class ArmorAdapter(
    private val armorList: List<Armor>,
    private val onItemClick: (Armor) -> Unit
) : RecyclerView.Adapter<ArmorAdapter.ArmorViewHolder>() {

    inner class ArmorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val armorImage: ImageView = itemView.findViewById(R.id.imageArmor)
        val armorName: TextView = itemView.findViewById(R.id.txtArmorName)
        val armorRarity: TextView = itemView.findViewById(R.id.txtArmorRarity)
        val armorDefense: TextView = itemView.findViewById(R.id.txtArmorDefense)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArmorViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_armor, parent, false)
        return ArmorViewHolder(view)
    }

    override fun onBindViewHolder(holder: ArmorViewHolder, position: Int) {
        val armor = armorList[position]

        holder.armorName.text = armor.name
        holder.armorRarity.text = buildString {
        append("Rareza: ")
        append(armor.rarity)
    }
        holder.armorDefense.text = buildString {
        append("Defensa: ")
        append(armor.defense)
    }


        val fileName = when (armor.armorType) {
            "head" -> "head_${armor.rarity}.png"
            "chest" -> "chest_${armor.rarity}.png"
            "arms" -> "arms_${armor.rarity}.png"
            "waist" -> "waist_${armor.rarity}.png"
            "legs" -> "legs_${armor.rarity}.png"
            else -> "default.png"
        }


        val path = "file:///android_asset/armor/$fileName"
        Glide.with(holder.itemView.context)
            .load(path)
            .into(holder.armorImage)


        holder.itemView.setOnClickListener {
            onItemClick(armor)
        }
    }

    override fun getItemCount(): Int = armorList.size
}
