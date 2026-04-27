package com.app.huntersclub.ui.sets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.appcompat.widget.SearchView
import com.app.huntersclub.HuntersClubApp
import com.app.huntersclub.data.dao.DecoDAO
import com.app.huntersclub.databinding.SelectDecorationBinding

class SelectDecoFragment : Fragment() {

    private var _binding: SelectDecorationBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: DecoAdapter
    private lateinit var decoDao: DecoDAO

    private val args: SelectDecoFragmentArgs by navArgs()

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


        val dbHelper = (requireActivity().application as HuntersClubApp).dbHelper
        decoDao = DecoDAO(dbHelper)


        val allDecorations = decoDao.getAllDecorations()
        val slotSize = args.slotSize
        val filteredBySlot = allDecorations.filter { it.slot <= slotSize }

        adapter = DecoAdapter { selectedDeco ->
            val result = Bundle().apply {
                putParcelable("selectedDeco", selectedDeco)
                putString("piece", args.piece)
                putInt("slotIndex", args.slotIndex)
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
