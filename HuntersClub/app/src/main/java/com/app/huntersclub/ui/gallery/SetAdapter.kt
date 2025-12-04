package com.app.huntersclub.ui.gallery

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.huntersclub.R
import com.app.huntersclub.model.Set

class SetAdapter(private val sets: List<Set>) :
    RecyclerView.Adapter<SetAdapter.SetViewHolder>() {

    class SetViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textWeapon: TextView = view.findViewById(R.id.txtWeapon)
        val textHead: TextView = view.findViewById(R.id.txtHead)
        val textTorso: TextView = view.findViewById(R.id.txtTorso)
        val textArms: TextView = view.findViewById(R.id.txtArms)
        val textWaist: TextView = view.findViewById(R.id.txtWaist)
        val textLegs: TextView = view.findViewById(R.id.txtLegs)
        val textCharm: TextView = view.findViewById(R.id.txtCharm)
        val textUser: TextView = view.findViewById(R.id.txtUser)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SetViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_set, parent, false)
        return SetViewHolder(view)
    }

    override fun onBindViewHolder(holder: SetViewHolder, position: Int) {
        val set = sets[position]
        holder.textWeapon.text = set.weaponName
        holder.textHead.text = set.armorHead
        holder.textTorso.text = set.armorChest
        holder.textArms.text = set.armorArms
        holder.textWaist.text = set.armorWaist
        holder.textLegs.text = set.armorLegs
        holder.textCharm.text = set.charm
        holder.textUser.text = buildString {
        append("Set creado por: ")
        append(set.createdBy)
    }
    }

    override fun getItemCount() = sets.size
}
