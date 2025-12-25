package com.app.huntersclub.ui.sets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.navigation.fragment.findNavController
import com.app.huntersclub.R
import com.app.huntersclub.databinding.FragmentSetsBinding
import com.app.huntersclub.data.dao.ArmorDAO
import com.app.huntersclub.data.dao.WeaponDAO
import com.app.huntersclub.data.dao.CharmDAO
import com.app.huntersclub.data.database.MyDatabaseHelper
import com.app.huntersclub.data.repository.SetRepository
import com.google.firebase.auth.FirebaseAuth

class SetsFragment : Fragment() {

    private var _binding: FragmentSetsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSetsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //Initialize DAOs and SetRepository
        val dbHelper = MyDatabaseHelper(requireContext())
        val weaponDao = WeaponDAO(dbHelper)
        val armorDao = ArmorDAO(dbHelper)
        val charmDao = CharmDAO(dbHelper)
        val setRepository = SetRepository(weaponDao, armorDao, charmDao)

        //ViewModel creation of the set repo
        val setsViewModel = ViewModelProvider(
            this,
            SetsViewModelFactory(setRepository)
        )[SetsViewModel::class.java]

        //Create set button visible only if user is logged in
        val currentUser = FirebaseAuth.getInstance().currentUser
        binding.btnCreateSet.visibility = if (currentUser != null) View.VISIBLE else View.GONE

        binding.btnCreateSet.setOnClickListener {
            findNavController().navigate(R.id.action_nav_gallery_to_createSetFragment)
        }

        binding.recyclerSets.layoutManager = LinearLayoutManager(requireContext())

        setsViewModel.sets.observe(viewLifecycleOwner) { setsList ->
            binding.recyclerSets.adapter = SetsAdapter(setsList)
        }

        //Real time listener
        setsViewModel.listenToSets()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
