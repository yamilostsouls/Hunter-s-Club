package com.app.huntersclub.ui.sets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.navigation.fragment.findNavController
import com.app.huntersclub.HuntersClubApp
import com.app.huntersclub.R
import com.app.huntersclub.databinding.FragmentSetsBinding
import com.app.huntersclub.utils.ItemDecoration
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
        val app = requireActivity().application as HuntersClubApp
        val setRepository = app.setRepository


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
        //Spacing for the sets so the border doesn't touch another border of other sets
        val space = (8 * resources.displayMetrics.density).toInt()
        binding.recyclerSets.addItemDecoration(ItemDecoration(space))

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
