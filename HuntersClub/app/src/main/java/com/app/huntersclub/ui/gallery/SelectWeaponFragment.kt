package com.app.huntersclub.ui.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.huntersclub.R
import com.app.huntersclub.data.MyDatabaseHelper
import com.app.huntersclub.data.WeaponDAO


class SelectWeaponFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var weaponDAO: WeaponDAO

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.select_weapon, container, false)

        recyclerView = view.findViewById(R.id.recyclerViewWeapon)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        weaponDAO = WeaponDAO(MyDatabaseHelper(requireContext()))

        val allWeapons = weaponDAO.getAllWeapons()

        val adapter = WeaponAdapter(allWeapons) { selectedWeapon ->
            val result = Bundle().apply {
                putParcelable("selectedWeapon", selectedWeapon)
            }
            setFragmentResult("weaponSelection", result)
            parentFragmentManager.popBackStack()
        }

        recyclerView.adapter = adapter

        return view
    }
}