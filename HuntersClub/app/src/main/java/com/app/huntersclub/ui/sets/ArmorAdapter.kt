package com.app.huntersclub.ui.sets

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.app.huntersclub.databinding.ItemArmorBinding
import com.app.huntersclub.model.Armor
import com.app.huntersclub.utils.ImagePath
import com.bumptech.glide.Glide

class ArmorAdapter(
    private val onItemClick: (Armor) -> Unit
) : ListAdapter<Armor, ArmorAdapter.ArmorViewHolder>(ArmorDiffCallback()), Filterable {

    private var armorList: List<Armor> = emptyList()

    inner class ArmorViewHolder(val binding: ItemArmorBinding) :
        androidx.recyclerview.widget.RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArmorViewHolder {
        val binding = ItemArmorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ArmorViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArmorViewHolder, position: Int) {
        val armor = getItem(position)

        holder.binding.txtArmorName.text = armor.name
        holder.binding.txtArmorRarity.text = buildString {
        append("Rareza: ")
        append(armor.rarity)
    }
        holder.binding.txtArmorDefense.text = buildString {
        append("Defensa: ")
        append(armor.defense)
    }
        val skillsText = armor.skills.joinToString("\n") { skill ->
            "${skill.name} Lv.${skill.level}" }

        holder.binding.txtArmorSkill.apply {
            text = skillsText
            visibility = if (armor.skills.isEmpty()) View.GONE else View.VISIBLE
        }

        val path = ImagePath.getAssetPath("armor", armor.rarity, armor.armorType)

        Glide.with(holder.itemView.context)
            .load(path)
            .into(holder.binding.imageArmor)

        holder.itemView.setOnClickListener { onItemClick(armor) }
    }

    fun setData(list: List<Armor>) {
        armorList = list
        submitList(list)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val query = constraint?.toString()?.trim()?.lowercase() ?: ""
                val filteredList = if (query.isEmpty()) {
                    armorList
                } else {
                    armorList.filter { it.name.lowercase().contains(query) }
                }
                return FilterResults().apply { values = filteredList }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                val newList = (results?.values as? List<Armor>).orEmpty()
                submitList(newList)
            }
        }
    }

    class ArmorDiffCallback : DiffUtil.ItemCallback<Armor>() {
        override fun areItemsTheSame(oldItem: Armor, newItem: Armor): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Armor, newItem: Armor): Boolean =
            oldItem == newItem
    }
}
