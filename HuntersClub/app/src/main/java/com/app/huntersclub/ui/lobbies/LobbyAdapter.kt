package com.app.huntersclub.ui.lobbies

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.app.huntersclub.R
import com.app.huntersclub.model.Lobby


class LobbyAdapter(
    private val monsterNames: Map<Int, String>,
    private val onClick: (Lobby) -> Unit
) : ListAdapter<Lobby, LobbyAdapter.LobbyViewHolder>(
    object : DiffUtil.ItemCallback<Lobby>() {
        override fun areItemsTheSame(a: Lobby, b: Lobby) = a.sessionId == b.sessionId
        override fun areContentsTheSame(a: Lobby, b: Lobby) = a == b
    }
) {

    inner class LobbyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtSession = view.findViewById<TextView>(R.id.txtLobbySessionId)
        val txtMonster = view.findViewById<TextView>(R.id.txtLobbyMonster)
        val txtLeader = view.findViewById<TextView>(R.id.txtLobbyLeader)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LobbyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_lobby, parent, false)
        return LobbyViewHolder(view)
    }

    override fun onBindViewHolder(holder: LobbyViewHolder, position: Int) {
        val lobby = getItem(position)

        val monsterName = lobby.monster?.let { monsterNames[it] } ?: "-"

        holder.txtSession.text = buildString {
        append("Sesión: ")
        append(lobby.sessionId)
    }
        holder.txtMonster.text = buildString {
        append("Monstruo: ")
        append(monsterName)
    }
        holder.txtLeader.text = buildString {
        append("Líder: ")
        append(lobby.hostName)
    }

        holder.itemView.setOnClickListener { onClick(lobby) }
    }
}
