package com.app.huntersclub.ui.lobbies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.huntersclub.HuntersClubApp
import com.app.huntersclub.R
import com.app.huntersclub.data.dao.MonsterDAO
import com.app.huntersclub.model.Monster
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class MonsterBottomSheet(
    private val onMonsterSelected: (Monster) -> Unit
) : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.bottomsheet_monster_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val dbHelper = (requireActivity().application as HuntersClubApp).dbHelper
        val dao = MonsterDAO(dbHelper)

        val monsters = dao.getAllMonsters()

        val recycler = view.findViewById<RecyclerView>(R.id.recyclerMonsters)
        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = MonsterListAdapter(monsters) { selected ->
            onMonsterSelected(selected)
            dismiss()
        }
    }
}