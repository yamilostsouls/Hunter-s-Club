package com.app.huntersclub.ui.lobbies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.huntersclub.HuntersClubApp
import com.app.huntersclub.R
import com.app.huntersclub.data.dao.MonsterDAO
import com.google.firebase.auth.FirebaseAuth

class LobbyDetailFragment : Fragment() {

    private lateinit var viewModel: LobbyDetailViewModel
    private lateinit var sessionId: String
    private lateinit var memberAdapter: MemberAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_lobby_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sessionId = arguments?.getString("sessionId") ?: ""

        viewModel = ViewModelProvider(this)[LobbyDetailViewModel::class.java]

        val txtSessionId = view.findViewById<TextView>(R.id.txtSessionId)
        val txtMonster = view.findViewById<TextView>(R.id.txtMonster)
        val txtHr = view.findViewById<TextView>(R.id.txtHr)
        val recyclerMembers = view.findViewById<RecyclerView>(R.id.recyclerMembers)

        val dbHelper = (requireActivity().application as HuntersClubApp).dbHelper
        val dao = MonsterDAO(dbHelper)

        val btnJoin = view.findViewById<Button>(R.id.btnJoin)
        val btnLeave = view.findViewById<Button>(R.id.btnLeave)

        memberAdapter = MemberAdapter()
        recyclerMembers.adapter = memberAdapter
        recyclerMembers.layoutManager = LinearLayoutManager(requireContext())


        btnJoin.setOnClickListener {
            viewModel.joinLobby(sessionId)
        }

        btnLeave.setOnClickListener {
            viewModel.leaveLobby(sessionId)
        }


        viewModel.lobby.observe(viewLifecycleOwner) { lobby ->

            txtSessionId.text = buildString {
        append("ID de la sala: ")
        append(lobby.sessionId)
    }

            val monster = lobby.monster?.let { dao.getMonsterById(it) }
            txtMonster.text = buildString {
        append("Monstruo: ")
        append(monster?.name ?: "-")
    }

            txtHr.text = buildString {
        append("Requisitos (HR/MR): ")
        append(lobby.hrRequirement ?: "-")
    }
        }


        viewModel.memberIds.observe(viewLifecycleOwner) { ids ->
            val currentUser = FirebaseAuth.getInstance().currentUser?.uid

            val isFull = ids.size >= 16
            val isLoggedIn = currentUser != null
            val isMember = isLoggedIn && ids.contains(currentUser)

            btnJoin.visibility = when {
                !isLoggedIn -> View.GONE
                isMember -> View.GONE
                isFull -> View.GONE
                else -> View.VISIBLE
            }

            btnLeave.visibility = when {
                !isLoggedIn -> View.GONE
                isMember -> View.VISIBLE
                else -> View.GONE
            }

        }

        viewModel.lobbyFull.observe(viewLifecycleOwner) {
            if (it) Toast.makeText(requireContext(), "La sala está llena", Toast.LENGTH_SHORT).show()
        }

        viewModel.memberNames.observe(viewLifecycleOwner) { names ->
            memberAdapter.submitList(names)
        }

        viewModel.loadLobby(sessionId)
        viewModel.loadMembers(sessionId)
    }

}
