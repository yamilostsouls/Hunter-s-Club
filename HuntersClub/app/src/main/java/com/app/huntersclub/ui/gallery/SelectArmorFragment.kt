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
import com.app.huntersclub.data.ArmorDAO
import com.app.huntersclub.data.MyDatabaseHelper

class SelectArmorFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var armorDao: ArmorDAO

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.select_armor, container, false)

        recyclerView = view.findViewById(R.id.recyclerViewArmor)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        armorDao = ArmorDAO(MyDatabaseHelper(requireContext()))

        val armorType = arguments?.getString("armorType") ?: "unknown"
        val allArmors = armorDao.getAllArmor()
        val filteredArmors = allArmors.filter { it.armorType == armorType }

        val adapter = ArmorAdapter(filteredArmors) { selectedArmor ->
            val result = Bundle().apply {
                putString("armorType", armorType)
                putParcelable("selectedArmor", selectedArmor)
            }
            setFragmentResult("armorSelection", result)
            parentFragmentManager.popBackStack()
        }

        recyclerView.adapter = adapter

        return view
    }
}
