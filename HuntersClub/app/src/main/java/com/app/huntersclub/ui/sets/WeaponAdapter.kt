package com.app.huntersclub.ui.sets

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.app.huntersclub.databinding.ItemWeaponBinding
import com.app.huntersclub.model.Weapon
import com.app.huntersclub.utils.ImagePath
import com.bumptech.glide.Glide

class WeaponAdapter(
    private val onItemClick: (Weapon) -> Unit
) : ListAdapter<Weapon, WeaponAdapter.WeaponViewHolder>(WeaponDiffCallback()) {

    private var weaponList: List<Weapon> = emptyList()

    inner class WeaponViewHolder(val binding: ItemWeaponBinding) :
        androidx.recyclerview.widget.RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeaponViewHolder {
        val binding = ItemWeaponBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WeaponViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WeaponViewHolder, position: Int) {
        val weapon = getItem(position)

        holder.binding.txtWeaponName.text = weapon.name
        holder.binding.txtWeaponRarity.text = buildString {
        append("Rareza: ")
        append(weapon.rarity)
    }
        holder.binding.txtWeaponAtk.text = buildString {
        append("Daño: ")
        append(weapon.atk)
    }
        //Since weapons doesn't have a skill
        //Or some weapons have only 1 skill
        //We can do it in this format. If we had any weapon with 2 skills
        //We should do list of skills.
        holder.binding.txtWeaponSkill.apply {
            if (weapon.skillName.isNullOrBlank() || weapon.skillLevel == 0) {
                visibility = View.GONE
            } else {
                text = buildString {
        append(weapon.skillName)
        append(" Lv.")
        append(weapon.skillLevel)
    }
                visibility = View.VISIBLE
            }
        }
        val path = ImagePath.getAssetPath("weapons", weapon.rarity, weapon.weaponType)

        Glide.with(holder.itemView.context).load(path).into(holder.binding.imageWeapon)

        holder.itemView.setOnClickListener { onItemClick(weapon) }
    }

    fun setData(list: List<Weapon>) {
        weaponList = list
        submitList(list)
    }
    //Custom filter for weapon buttons and text
    //Since someone wants to select a hammer and wants only the hammer list
    //A selectable button works wonders
    fun applyFilter(query: String?, types: Set<String>) {
        val filteredList = weaponList.filter { weapon ->
            val matchesName = query.isNullOrBlank() || weapon.name.lowercase().contains(query.lowercase())
            val matchesType = types.isEmpty() || types.contains(weapon.weaponType)
            matchesName && matchesType
        }
        submitList(filteredList)
    }

    class WeaponDiffCallback : DiffUtil.ItemCallback<Weapon>() {
        override fun areItemsTheSame(oldItem: Weapon, newItem: Weapon): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Weapon, newItem: Weapon): Boolean =
            oldItem == newItem
    }
}
