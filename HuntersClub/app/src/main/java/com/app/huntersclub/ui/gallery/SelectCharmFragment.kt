package com.app.huntersclub.ui.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.fragment.app.setFragmentResult
import com.app.huntersclub.R
import com.app.huntersclub.data.CharmDAO
import com.app.huntersclub.data.MyDatabaseHelper

class SelectCharmFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var charmDao: CharmDAO

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.select_charm, container, false)

        recyclerView = view.findViewById(R.id.recyclerViewCharm)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        charmDao = CharmDAO(MyDatabaseHelper(requireContext()))

        val allCharms = charmDao.getAllCharms()

        val adapter = CharmAdapter(allCharms) { selectedCharm ->
            val result = Bundle().apply {
                putParcelable("selectedCharm", selectedCharm)
            }
            setFragmentResult("charmSelection", result)
            parentFragmentManager.popBackStack()
        }

        recyclerView.adapter = adapter

        return view
    }
}



