package com.app.huntersclub.ui.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.huntersclub.data.CharmDAO
import com.app.huntersclub.data.MyDatabaseHelper
import com.app.huntersclub.databinding.SelectCharmBinding
import androidx.appcompat.widget.SearchView

class SelectCharmFragment : Fragment() {

    private var _binding: SelectCharmBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: CharmAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SelectCharmBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val dbHelper = MyDatabaseHelper(requireContext())
        dbHelper.createDatabase()
        val charmDao = CharmDAO(dbHelper)
        val charms = charmDao.getAllCharms()


        adapter = CharmAdapter { selectedCharm ->
            val result = Bundle().apply {
                putParcelable("selectedCharm", selectedCharm)
            }
            setFragmentResult("charmSelection", result)
            parentFragmentManager.popBackStack()
        }
        binding.recyclerViewCharm.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewCharm.adapter = adapter
        adapter.setData(charms)


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
