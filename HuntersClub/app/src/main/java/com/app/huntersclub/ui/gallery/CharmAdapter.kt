package com.app.huntersclub.ui.gallery

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.huntersclub.R
import com.app.huntersclub.model.Charm
import com.bumptech.glide.Glide

class CharmAdapter(
    private val charms: List<Charm>,
    private val onItemClick: (Charm) -> Unit
) : RecyclerView.Adapter<CharmAdapter.CharmViewHolder>() {

    inner class CharmViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageCharm: ImageView = itemView.findViewById(R.id.imageCharm)
        val textName: TextView = itemView.findViewById(R.id.txtCharmName)
        val textRarity: TextView = itemView.findViewById(R.id.txtCharmRarity)
        val textSkill: TextView = itemView.findViewById(R.id.txtCharmSkill)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharmViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_charm, parent, false)
        return CharmViewHolder(view)
    }

    override fun onBindViewHolder(holder: CharmViewHolder, position: Int) {
        val charm = charms[position]
        holder.textName.text = charm.name
        holder.textRarity.text = buildString {
        append("Rareza: ")
        append(charm.rarity)
    }
        holder.textSkill.text = buildString {
        append(charm.skillTreeName)
        append(" Lv.")
        append(charm.skillLevel)
    }

        //Commented code since we don't have the charms icons.
        //Once we have them, we uncomment this
        //val path = "file:///android_asset/charms/${charm.rarity}"
        //Glide.with(holder.itemView.context).load(path).into(holder.imageCharm)

        holder.itemView.setOnClickListener { onItemClick(charm) }
    }

    override fun getItemCount(): Int = charms.size
}

