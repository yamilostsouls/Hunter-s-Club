package com.app.huntersclub.ui.sets

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.huntersclub.R
import com.app.huntersclub.model.Set
import com.app.huntersclub.utils.DecoDrawableCache.loadDecorationDrawable
import com.app.huntersclub.utils.ImagePath.getAssetPath
import com.bumptech.glide.Glide

class SetsAdapter(private val sets: List<Set>) :
    RecyclerView.Adapter<SetsAdapter.SetViewHolder>() {

    class SetViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textName: TextView = view.findViewById(R.id.txtName)
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
            .inflate(R.layout.item_set, parent, false)
        return SetViewHolder(view)
    }

    override fun onBindViewHolder(holder: SetViewHolder, position: Int) {
        val set = sets[position]
        holder.textName.text = set.setName
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
        //Now we load images for better clarity
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

        loadDecoList(holder.layoutWeaponDecos, "weapon", set, holder)
        loadDecoList(holder.layoutHeadDecos, "head", set, holder)
        loadDecoList(holder.layoutChestDecos, "chest", set, holder)
        loadDecoList(holder.layoutArmsDecos, "arms", set, holder)
        loadDecoList(holder.layoutWaistDecos, "waist", set, holder)
        loadDecoList(holder.layoutLegsDecos, "legs", set, holder)

    }

    override fun getItemCount() = sets.size

    private fun loadDecoList(
        container: LinearLayout,
        prefix: String,
        set: Set,
        holder: SetViewHolder
    ) {
        container.removeAllViews()
        val context = holder.itemView.context

        val decos = set.decorations.filterKeys { it.startsWith(prefix) }

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

            val drawable = loadDecorationDrawable(context, deco.slot, deco.colour)
            img.setImageDrawable(drawable)

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

}
