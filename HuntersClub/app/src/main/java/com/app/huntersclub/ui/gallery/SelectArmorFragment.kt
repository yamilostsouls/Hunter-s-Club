package com.app.huntersclub.ui.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.huntersclub.data.ArmorDAO
import com.app.huntersclub.data.MyDatabaseHelper
import com.app.huntersclub.databinding.SelectArmorBinding
import androidx.appcompat.widget.SearchView

class SelectArmorFragment : Fragment() {

    private var _binding: SelectArmorBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ArmorAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SelectArmorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dbHelper = MyDatabaseHelper(requireContext())
        dbHelper.createDatabase()
        val armorDao = ArmorDAO(dbHelper)

        val armorType = arguments?.getString("armorType") ?: "unknown"
        val allArmors = armorDao.getAllArmor()
        val filteredArmors = allArmors.filter { it.armorType == armorType }

        adapter = ArmorAdapter { selectedArmor ->
            val result = Bundle().apply {
                putString("armorType", armorType)
                putParcelable("selectedArmor", selectedArmor)
            }
            setFragmentResult("armorSelection", result)
            parentFragmentManager.popBackStack()
        }

        binding.recyclerViewArmor.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewArmor.adapter = adapter
        adapter.setData(filteredArmors)


        binding.searchCharm.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                adapter.filter.filter(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return true
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
