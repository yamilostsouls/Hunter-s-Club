package com.app.huntersclub.ui.profile

import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.huntersclub.HuntersClubApp
import com.app.huntersclub.databinding.FragmentProfilesetsBinding
import com.app.huntersclub.utils.ItemDecoration

class ProfileSetsFragment : Fragment() {

    private var _binding: FragmentProfilesetsBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: ProfileSetsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfilesetsBinding.inflate(inflater, container, false)
        val root = binding.root

        //Initialize DAOs and repository
        val app = requireActivity().application as HuntersClubApp
        val setRepository = app.setRepository

        //ViewModel creation of the set repo
        val viewModel = ViewModelProvider(
            this,
            ProfileSetsViewModelFactory(setRepository)
        )[ProfileSetsViewModel::class.java]

        adapter = ProfileSetsAdapter { set ->
            showDeleteConfirmation {
                viewModel.deleteSet(set)
            }
        }

        //Spacing for the sets so the border doesn't touch another border of other sets
        binding.recyclerSets.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerSets.adapter = adapter
        val space = (8 * resources.displayMetrics.density).toInt()
        binding.recyclerSets.addItemDecoration(ItemDecoration(space))

        viewModel.userSets.observe(viewLifecycleOwner) { filteredSets ->
            adapter.submitList(filteredSets)
        }

        //Real time listener
        viewModel.listenToSets()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    //Dialog to confirm delete of a set
    private fun showDeleteConfirmation(onConfirm: () -> Unit) {
        androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle("Eliminar set")
            .setMessage("¿Seguro que quieres borrar este set?")
            .setPositiveButton("Sí") { _, _ -> onConfirm() }
            .setNegativeButton("Cancelar", null)
            .show()
    }


}
