package com.app.huntersclub.ui.lobbies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.app.huntersclub.R

class CreateLobbyFragment : Fragment() {

    private lateinit var viewModel: CreateLobbyViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_create_lobby, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[CreateLobbyViewModel::class.java]

        val edSessionId = view.findViewById<EditText>(R.id.edSessionId)
        val edMonster = view.findViewById<EditText>(R.id.edMonsterName)
        val edHr = view.findViewById<EditText>(R.id.edHrRequirement)
        val btnCreate = view.findViewById<Button>(R.id.btnCreateLobby)

        edMonster.setOnClickListener {
            MonsterBottomSheet { monster ->
                edMonster.setText(monster.name)
                edMonster.tag = monster.id
            }.show(parentFragmentManager, "MonsterSheet")
        }

        btnCreate.setOnClickListener {
            val sessionId = edSessionId.text.toString().trim()
            val monster = edMonster.tag as? Int
            val hr = edHr.text.toString().toIntOrNull()

            if (sessionId.length != 12) {
                Toast.makeText(context, "El ID debe tener 12 caracteres", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.lastSessionId = sessionId
            viewModel.createLobby(sessionId, monster, hr)
        }

        viewModel.createResult.observe(viewLifecycleOwner) { event ->
            val success = event.getIfNotHandled() ?: return@observe

            if (success) {
                val id = viewModel.lastSessionId
                val action = CreateLobbyFragmentDirections
                    .actionCreateLobbyFragmentToLobbyDetailFragment(id)
                findNavController().navigate(action)
            }
        }
    }
}
