package com.app.huntersclub.ui.monster

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.app.huntersclub.databinding.ItemMonsterBinding
import com.app.huntersclub.model.Monster
import com.app.huntersclub.utils.ImagePath
import com.bumptech.glide.Glide

class MonsterAdapter(
    val onItemClickListener: ((Monster) -> Unit)? = null
) : ListAdapter<Monster, MonsterAdapter.MonsterViewHolder>(MonsterDiffCallback()), Filterable {


    private var monsters: List<Monster> = emptyList()

    inner class MonsterViewHolder(val binding: ItemMonsterBinding) :
        androidx.recyclerview.widget.RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonsterViewHolder {
        val binding = ItemMonsterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MonsterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MonsterViewHolder, position: Int) {
        val monster = getItem(position)

        holder.binding.monsterName.text = monster.name
        holder.binding.monsterCategory.text = monster.monCategory

        //Load image from app\src\main\assets\monsters using Glide to obtain better performance
        //Using the standard loading from assets has a heavy impact on performance
        //By using Glide, performance increases and we don't have small stutters/lag on application
        val context = holder.itemView.context
        val path = ImagePath.getAssetPath("monsters", id = monster.id)

        Glide.with(context)
            .load(path)
            .into(holder.binding.monsterImage)

        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(monster)
        }
    }


    fun setData(monsters: List<Monster>) {
        this.monsters = monsters
        submitList(monsters)
    }

    //Searcher implementation to search monsters by its name
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val query = constraint?.toString()?.trim()
                val filteredList = if (query.isNullOrEmpty()) {
                    monsters
                } else {
                    monsters.filter {
                        it.name.contains(query, ignoreCase = true)
                    }
                }
                return FilterResults().apply { values = filteredList }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                val newList = (results?.values as? List<Monster>).orEmpty()
                submitList(newList)
            }
        }
    }
    //We use DiffUtil to compare two list and update the RecyclerView efficiently
    //We gain performance when searching for a monster because the monster list updates constantly
    class MonsterDiffCallback : DiffUtil.ItemCallback<Monster>() {
        override fun areItemsTheSame(oldItem: Monster, newItem: Monster): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Monster, newItem: Monster): Boolean =
            oldItem == newItem
    }
}