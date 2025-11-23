package com.app.huntersclub.ui.home

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.app.huntersclub.databinding.ItemMonsterBinding
import com.app.huntersclub.model.Monster

class MonsterAdapter(
    private val onItemClickListener: ((Monster) -> Unit)? = null
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

        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(monster)
        }
    }


    fun setData(monsters: List<Monster>) {
        this.monsters = monsters
        submitList(monsters)
    }

    //Searcher implementation
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val query = constraint?.toString()?.trim()
                val filteredList = if (query.isNullOrEmpty()) {
                    monsters
                } else {
                    monsters.filter {
                        it.name.contains(query, ignoreCase = true) ||
                                it.monCategory.contains(query, ignoreCase = true)
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

    class MonsterDiffCallback : DiffUtil.ItemCallback<Monster>() {
        override fun areItemsTheSame(oldItem: Monster, newItem: Monster): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Monster, newItem: Monster): Boolean =
            oldItem == newItem
    }
}