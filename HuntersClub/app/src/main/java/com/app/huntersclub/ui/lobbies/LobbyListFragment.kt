package com.app.huntersclub.ui.lobbies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.huntersclub.HuntersClubApp
import com.app.huntersclub.R
import com.app.huntersclub.data.dao.MonsterDAO
import com.google.firebase.auth.FirebaseAuth

class LobbyListFragment : Fragment() {

    private lateinit var viewModel: LobbyListViewModel
    private lateinit var adapter: LobbyAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_lobby_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[LobbyListViewModel::class.java]

        val recycler = view.findViewById<RecyclerView>(R.id.recyclerLobbies)
        recycler.layoutManager = LinearLayoutManager(requireContext())

        val dbHelper = (requireActivity().application as HuntersClubApp).dbHelper
        val dao = MonsterDAO(dbHelper)
        val monsters = dao.getAllMonsters()

        val monsterMap = monsters.associate { it.id to it.name }

        adapter = LobbyAdapter(monsterMap) { lobby ->

            val action = LobbyListFragmentDirections
                .actionLobbyListFragmentToLobbyDetailFragment(lobby.sessionId)
            findNavController().navigate(action)
        }

        recycler.adapter = adapter

        val btn = view.findViewById<Button>(R.id.btnCreateLobby)
        val currentUser = FirebaseAuth.getInstance().currentUser
        btn.visibility = if (currentUser == null) View.GONE else View.VISIBLE
        btn.setOnClickListener {
            val user = FirebaseAuth.getInstance().currentUser
            if (user == null) return@setOnClickListener

            val action = LobbyListFragmentDirections
                .actionLobbyListFragmentToCreateLobbyFragment()
            findNavController().navigate(action)
        }

        viewModel.lobbies.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
        }

        viewModel.loadRecentLobbies()
    }

}

