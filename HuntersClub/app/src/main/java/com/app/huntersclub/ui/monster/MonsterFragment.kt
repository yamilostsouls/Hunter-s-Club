package com.app.huntersclub.ui.monster

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.huntersclub.HuntersClubApp
import com.app.huntersclub.data.dao.MonsterDAO
import com.app.huntersclub.databinding.FragmentMonsterBinding

/**
 * Monster Fragment: List of the monsters with it's searchable
 *
 */

class MonsterFragment : Fragment() {

    private var _binding: FragmentMonsterBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: MonsterAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMonsterBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * Here we initialize the DB and DAO
     * Retrieve the monster list
     * Use the MonsterAdapter to configure the monster list
     * A separator for clarity
     * And the searcher configuration
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        val dbHelper = (requireActivity().application as HuntersClubApp).dbHelper
        val dao = MonsterDAO(dbHelper)

        val monsters = dao.getAllMonsters()


        adapter = MonsterAdapter { monster ->
            val action = MonsterFragmentDirections.actionNavHomeToMonsterDetailFragment(monster.id)
            findNavController().navigate(action)
        }
        binding.recyclerView.adapter = adapter
        adapter.setData(monsters)


        val dividerItemDecoration = DividerItemDecoration(
            binding.recyclerView.context,
            LinearLayoutManager.VERTICAL
        )
        binding.recyclerView.addItemDecoration(dividerItemDecoration)


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
