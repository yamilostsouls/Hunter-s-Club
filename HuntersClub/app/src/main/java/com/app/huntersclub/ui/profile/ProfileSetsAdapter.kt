package com.app.huntersclub.ui.profile


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.app.huntersclub.R
import com.app.huntersclub.model.Set
import com.app.huntersclub.utils.DecoDrawableCache.loadDecorationDrawable
import com.app.huntersclub.utils.ImagePath.getAssetPath
import com.bumptech.glide.Glide

class ProfileSetsAdapter(
    private val onDeleteClick: (Set) -> Unit
) : ListAdapter<Set, ProfileSetsAdapter.SetViewHolder>(SetDiffCallback()) {


    class SetDiffCallback : DiffUtil.ItemCallback<Set>() {
        override fun areItemsTheSame(oldItem: Set, newItem: Set): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Set, newItem: Set): Boolean =
            oldItem == newItem
    }

    class SetViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textName: TextView = view.findViewById(R.id.txtName)
        val btnDelete: Button = view.findViewById(R.id.btnDelete)
        val layoutDetails: LinearLayout = view.findViewById(R.id.layoutDetails)

        val textWeapon: TextView = view.findViewById(R.id.txtWeapon)
        val imageWeapon: ImageView = view.findViewById(R.id.imgWeapon)
        val textHead: TextView = view.findViewById(R.id.txtHead)
        val imageHead: ImageView = view.findViewById(R.id.imgHead)
        val textChest: TextView = view.findViewById(R.id.txtChest)
        val imageChest: ImageView = view.findViewById(R.id.imgChest)
        val textArms: TextView = view.findViewById(R.id.txtArms)
        val imageArms: ImageView = view.findViewById(R.id.imgArms)
        val textWaist: TextView = view.findViewById(R.id.txtWaist)
        val imageWaist: ImageView = view.findViewById(R.id.imgWaist)
        val textLegs: TextView = view.findViewById(R.id.txtLegs)
        val imageLegs: ImageView = view.findViewById(R.id.imgLegs)
        val textCharm: TextView = view.findViewById(R.id.txtCharm)
        val imageCharm: ImageView = view.findViewById(R.id.imgCharm)
        val textUser: TextView = view.findViewById(R.id.txtUser)

        val layoutWeaponDecos: LinearLayout = view.findViewById(R.id.layoutWeaponDecos)
        val layoutHeadDecos: LinearLayout = view.findViewById(R.id.layoutHeadDecos)
        val layoutChestDecos: LinearLayout = view.findViewById(R.id.layoutChestDecos)
        val layoutArmsDecos: LinearLayout = view.findViewById(R.id.layoutArmsDecos)
        val layoutWaistDecos: LinearLayout = view.findViewById(R.id.layoutWaistDecos)
        val layoutLegsDecos: LinearLayout = view.findViewById(R.id.layoutLegsDecos)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SetViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_profile_set, parent, false)
        return SetViewHolder(view)
    }

    override fun onBindViewHolder(holder: SetViewHolder, position: Int) {
        val set = getItem(position)

        holder.textName.text = set.setName

        holder.btnDelete.setOnClickListener {
            onDeleteClick(set)
        }


        holder.layoutDetails.visibility = View.GONE
        holder.textName.setOnClickListener {
            holder.layoutDetails.visibility =
                if (holder.layoutDetails.isVisible) View.GONE else View.VISIBLE
        }


        holder.textWeapon.text = set.weaponName
        holder.textHead.text = set.armorHead
        holder.textChest.text = set.armorChest
        holder.textArms.text = set.armorArms
        holder.textWaist.text = set.armorWaist
        holder.textLegs.text = set.armorLegs
        holder.textCharm.text = set.charm
        holder.textUser.text = buildString {
        append("Set creado por: ")
        append(set.createdBy)
    }


        Glide.with(holder.itemView.context)
            .load(getAssetPath("weapons", set.weaponRarity, set.weaponType))
            .into(holder.imageWeapon)

        Glide.with(holder.itemView.context)
            .load(getAssetPath("armor", set.armorHeadRarity, set.armorHeadType))
            .into(holder.imageHead)

        Glide.with(holder.itemView.context)
            .load(getAssetPath("armor", set.armorChestRarity, set.armorChestType))
            .into(holder.imageChest)

        Glide.with(holder.itemView.context)
            .load(getAssetPath("armor", set.armorArmsRarity, set.armorArmsType))
            .into(holder.imageArms)

        Glide.with(holder.itemView.context)
            .load(getAssetPath("armor", set.armorWaistRarity, set.armorWaistType))
            .into(holder.imageWaist)

        Glide.with(holder.itemView.context)
            .load(getAssetPath("armor", set.armorLegsRarity, set.armorLegsType))
            .into(holder.imageLegs)

        Glide.with(holder.itemView.context)
            .load(getAssetPath("charms", set.charmRarity))
            .into(holder.imageCharm)


        fun loadDecos(container: LinearLayout, prefix: String) {
            container.removeAllViews()
            val decos = set.decorations.filterKeys { it.startsWith(prefix) }

            val context = holder.itemView.context

            decos.forEach { (_, deco) ->
                val row = LinearLayout(context).apply {
                    orientation = LinearLayout.HORIZONTAL
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    ).apply { bottomMargin = 4 }
                }

                val img = ImageView(context)
                val size = holder.textWeapon.textSize.toInt()
                img.layoutParams = LinearLayout.LayoutParams(size, size)

                img.setImageDrawable(
                    loadDecorationDrawable(context, deco.slot, deco.colour)
                )

                val txt = TextView(context).apply {
                    text = deco.name
                    setPadding(8, 0, 0, 0)
                    textSize = 12f
                }

                row.addView(img)
                row.addView(txt)
                container.addView(row)
            }
        }


        loadDecos(holder.layoutWeaponDecos, "weapon")
        loadDecos(holder.layoutHeadDecos, "head")
        loadDecos(holder.layoutChestDecos, "chest")
        loadDecos(holder.layoutArmsDecos, "arms")
        loadDecos(holder.layoutWaistDecos, "waist")
        loadDecos(holder.layoutLegsDecos, "legs")
    }

}
