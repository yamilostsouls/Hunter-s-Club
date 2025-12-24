package com.app.huntersclub.ui.sets

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.app.huntersclub.databinding.ItemDecorationBinding
import com.app.huntersclub.model.Decoration

class DecoAdapter(
    private val onItemClick: (Decoration) -> Unit
) : ListAdapter<Decoration, DecoAdapter.DecoViewHolder>(DecoDiffCallback()), Filterable {

    private var decoList: List<Decoration> = emptyList()

    inner class DecoViewHolder(val binding: ItemDecorationBinding) :
        androidx.recyclerview.widget.RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DecoViewHolder {
        val binding = ItemDecorationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DecoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DecoViewHolder, position: Int) {
        val deco = getItem(position)

        holder.binding.txtDecoName.text = deco.name
        holder.binding.txtDecoRarity.text = buildString {
        append("Rareza: ")
        append(deco.rarity)
    }

        val skillsText = deco.skills.joinToString("\n") { skill ->
            "${skill.name} Lv.${skill.level}"
        }
        holder.binding.txtDecoSkill.text = skillsText

        //We have to check the logic of loading deco images since
        //Decorations can share the same image with other decorations.
        //We will use Glide + ImagePath.getAssetPath once we figure out
        //The logic of decoration images
        /*Glide.with(holder.itemView.context)
            .load(deco.imageDeco)
            .into(holder.imageDeco)*/

        holder.itemView.setOnClickListener { onItemClick(deco) }
    }

    fun setData(list: List<Decoration>) {
        decoList = list
        submitList(list)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val query = constraint?.toString()?.trim()?.lowercase() ?: ""

                val filteredList = if (query.isEmpty()) {
                    decoList
                } else {
                    decoList.filter { it.name.lowercase().contains(query)
                    }
                }

                return FilterResults().apply { values = filteredList }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                val newList = results?.values as? List<Decoration> ?: emptyList()
                submitList(newList)
            }
        }
    }

    class DecoDiffCallback : DiffUtil.ItemCallback<Decoration>() {
        override fun areItemsTheSame(oldItem: Decoration, newItem: Decoration): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Decoration, newItem: Decoration): Boolean =
            oldItem == newItem
    }
}



