package com.app.huntersclub.ui.gallery

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.huntersclub.R
import com.app.huntersclub.model.Weapon
import com.bumptech.glide.Glide

class WeaponAdapter (
    private val weapons: List<Weapon>,
    private val onItemClick: (Weapon) -> Unit
    ) : RecyclerView.Adapter<WeaponAdapter.WeaponViewHolder>() {

        inner class WeaponViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val imageWeapon: ImageView = itemView.findViewById(R.id.imageWeapon)
            val textName: TextView = itemView.findViewById(R.id.txtWeaponName)
            val textRarity: TextView = itemView.findViewById(R.id.txtWeaponRarity)
            val textDmg: TextView = itemView.findViewById(R.id.txtWeaponAtk)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeaponViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_weapon, parent, false)
            return WeaponViewHolder(view)
        }

        override fun onBindViewHolder(holder: WeaponViewHolder, position: Int) {
            val weapon = weapons[position]
            holder.textName.text = weapon.name
            holder.textRarity.text = buildString {
        append("Rareza: ")
        append(weapon.rarity)
    }
            holder.textDmg.text = buildString {
        append("Daño: ")
        append(weapon.atk)
    }

            //Loading weapon image commented since we don't have the images
            //Once we have them, we uncomment this section
            /*val fileName = when (weapon.weaponType) {
                "great-sword" -> "gs_${weapon.rarity}.png"
                "long-sword" -> "ls_${weapon.rarity}.png"
                "sword-and-shield" -> "sas_${weapon.rarity}.png"
                "dual-blades" -> "db_${weapon.rarity}.png"
                "hammer" -> "hammer_${weapon.rarity}.png"
                "hunting-horn" -> "hh_${weapon.rarity}.png"
                "lance" -> "lance_${weapon.rarity}.png"
                "gunlance" -> "gl_${weapon.rarity}.png"
                "switch-axe" -> "sa_${weapon.rarity}.png"
                "charge-blade" -> "cb_${weapon.rarity}.png"
                "insect-glaive" -> "ig_${weapon.rarity}.png"
                "light-bowgun" -> "lb_${weapon.rarity}.png"
                "heavy-bowgun" -> "hb_${weapon.rarity}.png"
                "bow" -> "bow_${weapon.rarity}.png"
                else -> "default.png"
            }


            val path = "file:///android_asset/weapon/$fileName"
            Glide.with(holder.itemView.context).load(path).into(holder.imageWeapon)*/

            holder.itemView.setOnClickListener { onItemClick(weapon) }
        }

        override fun getItemCount(): Int = weapons.size
}
