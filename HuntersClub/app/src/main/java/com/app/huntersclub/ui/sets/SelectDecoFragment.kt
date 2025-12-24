package com.app.huntersclub.ui.sets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.huntersclub.data.DecoDAO
import com.app.huntersclub.data.MyDatabaseHelper
import com.app.huntersclub.databinding.SelectDecorationBinding
import androidx.appcompat.widget.SearchView

class SelectDecoFragment : Fragment() {

    private var _binding: SelectDecorationBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: DecoAdapter
    private lateinit var decoDao: DecoDAO

    private var currentQuery: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SelectDecorationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dbHelper = MyDatabaseHelper(requireContext())
        dbHelper.createDatabase()
        decoDao = DecoDAO(dbHelper)

        val allDecorations = decoDao.getAllDecorations()

        val slotSize = arguments?.getInt("slotSize") ?: 4

        val filteredBySlot = allDecorations.filter { it.slot <= slotSize }

        adapter = DecoAdapter { selectedDeco ->
            val result = Bundle().apply {
                putParcelable("selectedDeco", selectedDeco)
                putInt("slotSize", slotSize)
            }
            setFragmentResult("decoSelection", result)
            parentFragmentManager.popBackStack()
        }

        binding.recyclerViewDecoration.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewDecoration.adapter = adapter

        adapter.setData(filteredBySlot)

        binding.searchDeco.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                currentQuery = query
                adapter.filter.filter(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                currentQuery = newText
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
