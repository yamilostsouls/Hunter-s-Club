package com.app.huntersclub.ui.gallery

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.huntersclub.R
import com.app.huntersclub.model.Decoration
import com.bumptech.glide.Glide

class DecoAdapter(
    private val decorations: List<Decoration>,
    private val onItemClick: (Decoration) -> Unit
) : RecyclerView.Adapter<DecoAdapter.DecoViewHolder>() {

    inner class DecoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageDeco: ImageView = itemView.findViewById(R.id.imageDeco)
        val textName: TextView = itemView.findViewById(R.id.txtDecoName)
        val textRarity: TextView = itemView.findViewById(R.id.txtDecoRarity)
        val textSkill: TextView = itemView.findViewById(R.id.txtDecoSkill)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DecoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_decoration, parent, false)
        return DecoViewHolder(view)
    }

    override fun onBindViewHolder(holder: DecoViewHolder, position: Int) {
        val deco = decorations[position]
        holder.textName.text = deco.name
        holder.textRarity.text = buildString {
        append("Rareza: ")
        append(deco.rarity)
    }
        holder.textSkill.text = buildString {
        append(deco.skillTreeName)
        append(" Lv.")
        append(deco.skillTreeLevel)
    }

        //We have to check the logic of loading deco images since
        //Decorations have their image linked to other decorations.
        //Glide.with(holder.itemView.context).load(deco.imageDeco).into(holder.imageDeco)

        holder.itemView.setOnClickListener { onItemClick(deco) }
    }

    override fun getItemCount(): Int = decorations.size
}