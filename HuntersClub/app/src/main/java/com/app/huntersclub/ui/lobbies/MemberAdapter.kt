package com.app.huntersclub.ui.lobbies

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.app.huntersclub.R

class MemberAdapter : ListAdapter<String, MemberAdapter.MemberViewHolder>(
    object : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(a: String, b: String) = a == b
        override fun areContentsTheSame(a: String, b: String) = a == b
    }
) {
    inner class MemberViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtName: TextView = view.findViewById(R.id.txtMemberName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_member, parent, false)
        return MemberViewHolder(view)
    }

    override fun onBindViewHolder(holder: MemberViewHolder, position: Int) {
        holder.txtName.text = getItem(position)
    }
}

