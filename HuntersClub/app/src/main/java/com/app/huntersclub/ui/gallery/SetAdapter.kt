package com.app.huntersclub.ui.gallery

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.huntersclub.R
import com.app.huntersclub.model.Set
import com.app.huntersclub.utils.ImageLoad.getAssetPath
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
            .load(getAssetPath("weapons", set.weaponRarity, set.weaponType))
            .into(holder.imageWeapon)
        Glide.with(holder.itemView.context)
            .load(getAssetPath("armor", set.armorHeadRarity, set.armorHeadType))
            .into(holder.imageHead)
        Glide.with(holder.itemView.context)
            .load(getAssetPath("armor", set.armorChestRarity, set.armorChestType))
            .into(holder.imageChest)
        Glide.with(holder.itemView.context)
            .load(getAssetPath("armor", set.armorArmsRarity, set.armorArmsType))
            .into(holder.imageArms)
        Glide.with(holder.itemView.context)
            .load(getAssetPath("armor", set.armorWaistRarity, set.armorWaistType))
            .into(holder.imageWaist)
        Glide.with(holder.itemView.context)
            .load(getAssetPath("armor", set.armorLegsRarity, set.armorLegsType))
            .into(holder.imageLegs)
        Glide.with(holder.itemView.context)
            .load(getAssetPath("charms", set.charmRarity))
            .into(holder.imageCharm)

    }

    override fun getItemCount() = sets.size
}
