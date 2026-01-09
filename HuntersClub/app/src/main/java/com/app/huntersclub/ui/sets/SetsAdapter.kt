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
import com.app.huntersclub.utils.ImagePath.getAssetPath
import com.bumptech.glide.Glide

class SetsAdapter(private val sets: List<Set>) :
    RecyclerView.Adapter<SetsAdapter.SetViewHolder>() {

    class SetViewHolder(view: View) : RecyclerView.ViewHolder(view) {
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

        //Section to load the decorations
        //At the right of the weapon or piece of armor
        //Containing the proper decoration image
        //And the name of the decoration
        //For older sets, it's fine since it doesn't show decorations
        holder.layoutWeaponDecos.removeAllViews()
        val weaponDecos = set.decorations.filterKeys { it.startsWith("weapon") }
        weaponDecos.forEach { (_, deco) ->
            val row = LinearLayout(holder.itemView.context).apply {
                orientation = LinearLayout.HORIZONTAL
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply { bottomMargin = 4 }
            }
            val img = ImageView(holder.itemView.context)
            val size = holder.textWeapon.textSize.toInt()
            img.layoutParams = LinearLayout.LayoutParams(size, size)
            Glide.with(holder.itemView)
                .load(getAssetPath("decorations", slot = deco.slot))
                .into(img)
            val txt = TextView(holder.itemView.context).apply {
                text = deco.name
                setPadding(8, 0, 0, 0)
                textSize = 12f
            }
            row.addView(img)
            row.addView(txt)
            holder.layoutWeaponDecos.addView(row)
        }

        holder.layoutHeadDecos.removeAllViews()
        val headDecos = set.decorations.filterKeys { it.startsWith("head") }
        headDecos.forEach { (_, deco) ->
            val row = LinearLayout(holder.itemView.context).apply {
                orientation = LinearLayout.HORIZONTAL
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply { bottomMargin = 4 }
            }
            val img = ImageView(holder.itemView.context)
            val size = holder.textWeapon.textSize.toInt()
            img.layoutParams = LinearLayout.LayoutParams(size, size)
            Glide.with(holder.itemView)
                .load(getAssetPath("decorations", slot = deco.slot))
                .into(img)
            val txt = TextView(holder.itemView.context).apply {
                text = deco.name
                setPadding(8, 0, 0, 0)
                textSize = 12f
            }
            row.addView(img)
            row.addView(txt)
            holder.layoutHeadDecos.addView(row)
        }

        holder.layoutChestDecos.removeAllViews()
        val chestDecos = set.decorations.filterKeys { it.startsWith("chest") }
        chestDecos.forEach { (_, deco) ->
            val row = LinearLayout(holder.itemView.context).apply {
                orientation = LinearLayout.HORIZONTAL
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply { bottomMargin = 4 }
            }
            val img = ImageView(holder.itemView.context)
            val size = holder.textWeapon.textSize.toInt()
            img.layoutParams = LinearLayout.LayoutParams(size, size)
            Glide.with(holder.itemView)
                .load(getAssetPath("decorations", slot = deco.slot))
                .into(img)
            val txt = TextView(holder.itemView.context).apply {
                text = deco.name
                setPadding(8, 0, 0, 0)
                textSize = 12f
            }
            row.addView(img)
            row.addView(txt)
            holder.layoutChestDecos.addView(row)
        }

        holder.layoutArmsDecos.removeAllViews()
        val armsDecos = set.decorations.filterKeys { it.startsWith("arms") }
        armsDecos.forEach { (_, deco) ->
            val row = LinearLayout(holder.itemView.context).apply {
                orientation = LinearLayout.HORIZONTAL
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply { bottomMargin = 4 }
            }
            val img = ImageView(holder.itemView.context)
            val size = holder.textWeapon.textSize.toInt()
            img.layoutParams = LinearLayout.LayoutParams(size, size)
            Glide.with(holder.itemView)
                .load(getAssetPath("decorations", slot = deco.slot))
                .into(img)
            val txt = TextView(holder.itemView.context).apply {
                text = deco.name
                setPadding(8, 0, 0, 0)
                textSize = 12f
            }
            row.addView(img)
            row.addView(txt)
            holder.layoutArmsDecos.addView(row)
        }

        holder.layoutWaistDecos.removeAllViews()
        val waistDecos = set.decorations.filterKeys { it.startsWith("waist") }
        waistDecos.forEach { (_, deco) ->
            val row = LinearLayout(holder.itemView.context).apply {
                orientation = LinearLayout.HORIZONTAL
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply { bottomMargin = 4 }
            }
            val img = ImageView(holder.itemView.context)
            val size = holder.textWeapon.textSize.toInt()
            img.layoutParams = LinearLayout.LayoutParams(size, size)
            Glide.with(holder.itemView)
                .load(getAssetPath("decorations", slot = deco.slot))
                .into(img)
            val txt = TextView(holder.itemView.context).apply {
                text = deco.name
                setPadding(8, 0, 0, 0)
                textSize = 12f
            }
            row.addView(img)
            row.addView(txt)
            holder.layoutWaistDecos.addView(row)
        }

        holder.layoutLegsDecos.removeAllViews()
        val legsDecos = set.decorations.filterKeys { it.startsWith("legs") }
        legsDecos.forEach { (_, deco) ->
            val row = LinearLayout(holder.itemView.context).apply {
                orientation = LinearLayout.HORIZONTAL
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply { bottomMargin = 4 }
            }
            val img = ImageView(holder.itemView.context)
            val size = holder.textWeapon.textSize.toInt()
            img.layoutParams = LinearLayout.LayoutParams(size, size)
            Glide.with(holder.itemView)
                .load(getAssetPath("decorations", slot = deco.slot))
                .into(img)
            val txt = TextView(holder.itemView.context).apply {
                text = deco.name
                setPadding(8, 0, 0, 0)
                textSize = 12f
            }
            row.addView(img)
            row.addView(txt)
            holder.layoutLegsDecos.addView(row)
        }
    }

    override fun getItemCount() = sets.size
}
