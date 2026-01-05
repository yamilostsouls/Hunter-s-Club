package com.app.huntersclub.ui.sets

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.app.huntersclub.databinding.ItemCharmBinding
import com.app.huntersclub.model.Charm
import com.app.huntersclub.utils.ImagePath
import com.bumptech.glide.Glide

class CharmAdapter(
    private val onItemClickListener: ((Charm) -> Unit)? = null
) : ListAdapter<Charm, CharmAdapter.CharmViewHolder>(CharmDiffCallback()), Filterable {

    private var charmList: List<Charm> = emptyList()

    inner class CharmViewHolder(val binding: ItemCharmBinding) :
        androidx.recyclerview.widget.RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharmViewHolder {
        val binding = ItemCharmBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CharmViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CharmViewHolder, position: Int) {
        val charm = getItem(position)

        holder.binding.txtCharmName.text = charm.name
        holder.binding.txtCharmRarity.text = buildString {
        append("Rareza: ")
        append(charm.rarity)
        }
        //Since skills now is in a list to not have duplicated pieces
        //We need to change how it's showed
        val skillsText = charm.skills.joinToString("\n") { skill ->
            "${skill.name} Lv.${skill.level}" }
        holder.binding.txtCharmSkill.text = skillsText

        val path = ImagePath.getAssetPath("charms", charm.rarity)

        Glide.with(holder.itemView.context)
            .load(path)
            .into(holder.binding.imageCharm)

        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(charm)
        }
    }

    fun setData(list: List<Charm>) {
        charmList = list
        submitList(list)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val query = constraint?.toString()?.trim()
                val filteredList = if (query.isNullOrEmpty()) {
                    charmList
                } else {
                    charmList.filter {
                        it.name.contains(query, ignoreCase = true)
                    }
                }
                return FilterResults().apply { values = filteredList }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                val newList = (results?.values as? List<Charm>).orEmpty()
                submitList(newList)
            }
        }
    }

    class CharmDiffCallback : DiffUtil.ItemCallback<Charm>() {
        override fun areItemsTheSame(oldItem: Charm, newItem: Charm): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Charm, newItem: Charm): Boolean =
            oldItem == newItem
    }
}
