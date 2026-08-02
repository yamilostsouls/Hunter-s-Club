package com.app.huntersclub.ui.lobbies

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.huntersclub.R
import com.app.huntersclub.model.Monster

class MonsterListAdapter(
    private val monsters: List<Monster>,
    private val onClick: (Monster) -> Unit
) : RecyclerView.Adapter<MonsterListAdapter.MonsterViewHolder>() {

    inner class MonsterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtName: TextView = view.findViewById(R.id.txtMonsterName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonsterViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_monster_name, parent, false)
        return MonsterViewHolder(view)
    }

    override fun onBindViewHolder(holder: MonsterViewHolder, position: Int) {
        val monster = monsters[position]
        holder.txtName.text = monster.name
        holder.itemView.setOnClickListener { onClick(monster) }
    }

    override fun getItemCount() = monsters.size
}
