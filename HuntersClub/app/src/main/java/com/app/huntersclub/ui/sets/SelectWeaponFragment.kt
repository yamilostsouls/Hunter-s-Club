package com.app.huntersclub.ui.sets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.huntersclub.data.database.MyDatabaseHelper
import com.app.huntersclub.data.dao.WeaponDAO
import com.app.huntersclub.databinding.SelectWeaponBinding
import androidx.appcompat.widget.SearchView

class SelectWeaponFragment : Fragment() {

    private var _binding: SelectWeaponBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: WeaponAdapter

    private var currentQuery: String? = null
    private val selectedTypes = mutableSetOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SelectWeaponBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dbHelper = MyDatabaseHelper(requireContext())
        dbHelper.createDatabase()
        val weaponDAO = WeaponDAO(dbHelper)

        val allWeapons = weaponDAO.getAllWeapons()

        adapter = WeaponAdapter { selectedWeapon ->
            val result = Bundle().apply {
                putParcelable("selectedWeapon", selectedWeapon)
            }
            setFragmentResult("weaponSelection", result)
            parentFragmentManager.popBackStack()
        }

        binding.recyclerViewWeapon.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewWeapon.adapter = adapter
        adapter.setData(allWeapons)

        val allButtons = listOf(
            binding.btnFilterGS, binding.btnFilterLS, binding.btnFilterSAS,
            binding.btnFilterDB, binding.btnFilterHammer, binding.btnFilterHH,
            binding.btnFilterLance, binding.btnFilterGL, binding.btnFilterSA,
            binding.btnFilterCB, binding.btnFilterIG, binding.btnFilterLB,
            binding.btnFilterHB, binding.btnFilterBow
        )
        allButtons.forEach { it.alpha = 0.5f }

        binding.searchWeapon.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                currentQuery = query
                adapter.applyFilter(currentQuery, selectedTypes)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                currentQuery = newText
                adapter.applyFilter(currentQuery, selectedTypes)
                return true
            }
        })

        fun toggleType(type: String, button: ImageButton) {
            if (selectedTypes.contains(type)) {
                selectedTypes.remove(type)
                button.alpha = 0.5f
            } else {
                selectedTypes.add(type)
                button.alpha = 1.0f
            }
            adapter.applyFilter(currentQuery, selectedTypes)
        }

        binding.btnFilterGS.setOnClickListener { toggleType("great-sword", binding.btnFilterGS) }
        binding.btnFilterLS.setOnClickListener { toggleType("long-sword", binding.btnFilterLS) }
        binding.btnFilterSAS.setOnClickListener { toggleType("sword-and-shield", binding.btnFilterSAS) }
        binding.btnFilterDB.setOnClickListener { toggleType("dual-blades", binding.btnFilterDB) }
        binding.btnFilterHammer.setOnClickListener { toggleType("hammer", binding.btnFilterHammer) }
        binding.btnFilterHH.setOnClickListener { toggleType("hunting-horn", binding.btnFilterHH) }
        binding.btnFilterLance.setOnClickListener { toggleType("lance", binding.btnFilterLance) }
        binding.btnFilterGL.setOnClickListener { toggleType("gunlance", binding.btnFilterGL) }
        binding.btnFilterSA.setOnClickListener { toggleType("switch-axe", binding.btnFilterSA) }
        binding.btnFilterCB.setOnClickListener { toggleType("charge-blade", binding.btnFilterCB) }
        binding.btnFilterIG.setOnClickListener { toggleType("insect-glaive", binding.btnFilterIG) }
        binding.btnFilterLB.setOnClickListener { toggleType("light-bowgun", binding.btnFilterLB) }
        binding.btnFilterHB.setOnClickListener { toggleType("heavy-bowgun", binding.btnFilterHB) }
        binding.btnFilterBow.setOnClickListener { toggleType("bow", binding.btnFilterBow) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
