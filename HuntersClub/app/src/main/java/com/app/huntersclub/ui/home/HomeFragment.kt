package com.app.huntersclub.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.huntersclub.data.MonsterDAO
import com.app.huntersclub.data.MyDatabaseHelper
import com.app.huntersclub.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: MonsterAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        //Initialize DB and DAO
        val dbHelper = MyDatabaseHelper(requireContext())
        dbHelper.createDatabase()
        val dao = MonsterDAO(dbHelper)

        //Get monster list
        val monsters = dao.getAllMonsters()

        //Adapter configuration for monster list
        adapter = MonsterAdapter { monster ->
            val action = HomeFragmentDirections.actionNavHomeToMonsterDetailFragment(monster.id)
            findNavController().navigate(action)
        }
        binding.recyclerView.adapter = adapter
        adapter.setData(monsters)


        //Separator line between list elements for clarity
        val dividerItemDecoration = DividerItemDecoration(
            binding.recyclerView.context,
            LinearLayoutManager.VERTICAL
        )
        binding.recyclerView.addItemDecoration(dividerItemDecoration)

        //Searcher configuration
        binding.searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
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
