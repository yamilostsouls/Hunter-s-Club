package com.app.huntersclub.ui.gallery

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.app.huntersclub.databinding.ItemCharmBinding
import com.app.huntersclub.model.Charm
import com.bumptech.glide.Glide

class CharmAdapter(
    private val onItemClickListener: ((Charm) -> Unit)? = null
) : ListAdapter<Charm, CharmAdapter.CharmViewHolder>(CharmDiffCallback()), Filterable {

    private var charms: List<Charm> = emptyList()

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
        val skillsText = charm.skills.joinToString(separator = "\n") { skill ->
            "${skill.name} Lv.${skill.level}"
        }
        holder.binding.txtCharmSkill.text = skillsText

        val context = holder.itemView.context
        val imagePath = "file:///android_asset/charms/${charm.rarity}.png"

        Glide.with(context)
            .load(imagePath)
            .into(holder.binding.imageCharm)

        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(charm)
        }
    }

    fun setData(charms: List<Charm>) {
        this.charms = charms
        submitList(charms)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val query = constraint?.toString()?.trim()
                val filteredList = if (query.isNullOrEmpty()) {
                    charms
                } else {
                    charms.filter {
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
