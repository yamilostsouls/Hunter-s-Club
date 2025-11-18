package com.app.huntersclub.ui.home

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.app.huntersclub.databinding.ItemMonsterBinding
import com.app.huntersclub.model.Monster

class MonsterAdapter(private var monsters: List<Monster>) :
    RecyclerView.Adapter<MonsterAdapter.MonsterViewHolder>(), Filterable {

    private var monstersFiltered: List<Monster> = monsters

    inner class MonsterViewHolder(val binding: ItemMonsterBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonsterViewHolder {
        val binding = ItemMonsterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MonsterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MonsterViewHolder, position: Int) {
        val monster = monstersFiltered[position]
        holder.binding.monsterName.text = monster.name
        holder.binding.monsterCategory.text = monster.monCategory

        //Load image from app\src\main\assets\monsters
        try {
            val inputStream = holder.itemView.context.assets.open(monster.imagenResId)
            val drawable = Drawable.createFromStream(inputStream, null)
            holder.binding.monsterImage.setImageDrawable(drawable)
            inputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
            holder.binding.monsterImage.setImageResource(android.R.drawable.ic_menu_report_image)
        }
    }

    override fun getItemCount() = monstersFiltered.size

    //Method to update list
    fun updateData(newList: List<Monster>) {
        monsters = newList
        monstersFiltered = newList
        notifyDataSetChanged()
    }

    //Searcher implementation
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val query = constraint?.toString()?.lowercase()?.trim()
                val filteredList = if (query.isNullOrEmpty()) {
                    monsters
                } else {
                    monsters.filter {
                        it.name.lowercase().contains(query) ||
                                it.monCategory.lowercase().contains(query)
                    }
                }
                return FilterResults().apply { values = filteredList }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                monstersFiltered = results?.values as List<Monster>
                notifyDataSetChanged()
            }
        }
    }
}
