package com.app.huntersclub.ui.gallery

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.huntersclub.R
import com.app.huntersclub.model.Set
import com.bumptech.glide.Glide

class SetAdapter(private val sets: List<Set>) :
    RecyclerView.Adapter<SetAdapter.SetViewHolder>() {

    class SetViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textWeapon: TextView = view.findViewById(R.id.txtWeapon)
        val imageWeapon: ImageView = view.findViewById(R.id.imgWeapon)
        val textHead: TextView = view.findViewById(R.id.txtHead)
        val imageHead: ImageView = view.findViewById(R.id.imgHead)
        val textChest: TextView = view.findViewById(R.id.txtChest)
        val imageChest: ImageView = view.findViewById(R.id.imgChest)
        val textArms: TextView = view.findViewById(R.id.txtArms)
        val imageArms: ImageView = view.findViewById(R.id.imgArms)
        val textWaist: TextView = view.findViewById(R.id.txtWaist)
        val imageWaist: ImageView = view.findViewById(R.id.imgWaist)
        val textLegs: TextView = view.findViewById(R.id.txtLegs)
        val imageLegs: ImageView = view.findViewById(R.id.imgLegs)
        val textCharm: TextView = view.findViewById(R.id.txtCharm)
        val imageCharm: ImageView = view.findViewById(R.id.imgCharm)
        val textUser: TextView = view.findViewById(R.id.txtUser)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SetViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_set, parent, false)
        return SetViewHolder(view)
    }

    override fun onBindViewHolder(holder: SetViewHolder, position: Int) {
        val set = sets[position]
        holder.textWeapon.text = set.weaponName
        holder.textHead.text = set.armorHead
        holder.textChest.text = set.armorChest
        holder.textArms.text = set.armorArms
        holder.textWaist.text = set.armorWaist
        holder.textLegs.text = set.armorLegs
        holder.textCharm.text = set.charm
        holder.textUser.text = buildString {
        append("Set creado por: ")
        append(set.createdBy)
        }
        //Now we load images for better clarity
        Glide.with(holder.itemView.context)
            .load(getRarityAssetPath("weapons", set.weaponRarity, set.weaponType))
            .into(holder.imageWeapon)
        Glide.with(holder.itemView.context)
            .load(getRarityAssetPath("armor", set.armorHeadRarity, set.armorHeadType))
            .into(holder.imageHead)
        Glide.with(holder.itemView.context)
            .load(getRarityAssetPath("armor", set.armorChestRarity, set.armorChestType))
            .into(holder.imageChest)
        Glide.with(holder.itemView.context)
            .load(getRarityAssetPath("armor", set.armorArmsRarity, set.armorArmsType))
            .into(holder.imageArms)
        Glide.with(holder.itemView.context)
            .load(getRarityAssetPath("armor", set.armorWaistRarity, set.armorWaistType))
            .into(holder.imageWaist)
        Glide.with(holder.itemView.context)
            .load(getRarityAssetPath("armor", set.armorLegsRarity, set.armorLegsType))
            .into(holder.imageLegs)
        Glide.with(holder.itemView.context)
            .load(getRarityAssetPath("charms", set.charmRarity))
            .into(holder.imageCharm)

    }

    //Function to obtain the images from assets and load them on the GalleryFragment
    //(the set section shared by users)
    private fun getRarityAssetPath(type: String, rarity: Int, subtype: String? = null): String {

        return when (type) {

            "charms" -> {
                //assets/charms/5.png
                "file:///android_asset/charms/$rarity.png"
            }

            "weapons" -> {
                //Internal conversion for weapon types
                //Same as on WeaponAdapter
                val prefix = when (subtype) {
                    "great-sword" -> "gs"
                    "long-sword" -> "ls"
                    "sword-and-shield" -> "sas"
                    "dual-blades" -> "db"
                    "hammer" -> "hammer"
                    "hunting-horn" -> "hh"
                    "lance" -> "lance"
                    "gunlance" -> "gl"
                    "switch-axe" -> "sa"
                    "charge-blade" -> "cb"
                    "insect-glaive" -> "ig"
                    "light-bowgun" -> "lb"
                    "heavy-bowgun" -> "hb"
                    "bow" -> "bow"
                    else -> "default"
                }
                //assets/weapons/gs_12
                "file:///android_asset/weapons/${prefix}_${rarity}.png"
            }

            "armor" -> {
                //assets/armor/head_4.png
                val armorType = subtype ?: "unknown"
                "file:///android_asset/armor/${armorType}_${rarity}.png"
            }

            else -> "file:///android_asset/default.png"
        }
    }

    override fun getItemCount() = sets.size
}
