package com.app.huntersclub.ui.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.navigation.fragment.findNavController
import com.app.huntersclub.R
import com.app.huntersclub.databinding.FragmentGalleryBinding
import com.app.huntersclub.data.ArmorDAO
import com.app.huntersclub.data.WeaponDAO
import com.app.huntersclub.data.CharmDAO
import com.app.huntersclub.data.MyDatabaseHelper
import com.app.huntersclub.data.SetRepository
import com.google.firebase.auth.FirebaseAuth

class GalleryFragment : Fragment() {

    private var _binding: FragmentGalleryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //Initialize DAOs and SetRepository
        val dbHelper = MyDatabaseHelper(requireContext())
        val weaponDao = WeaponDAO(dbHelper)
        val armorDao = ArmorDAO(dbHelper)
        val charmDao = CharmDAO(dbHelper)
        val setRepository = SetRepository(weaponDao, armorDao, charmDao)

        //ViewModel creation of the set repo
        val galleryViewModel = ViewModelProvider(
            this,
            GalleryViewModelFactory(setRepository)
        )[GalleryViewModel::class.java]

        //Create set button visible only if user is logged in
        val currentUser = FirebaseAuth.getInstance().currentUser
        binding.btnCreateSet.visibility = if (currentUser != null) View.VISIBLE else View.GONE

        binding.btnCreateSet.setOnClickListener {
            findNavController().navigate(R.id.action_nav_gallery_to_createSetFragment)
        }

        binding.recyclerSets.layoutManager = LinearLayoutManager(requireContext())

        galleryViewModel.sets.observe(viewLifecycleOwner) { setsList ->
            binding.recyclerSets.adapter = SetAdapter(setsList)
        }

        //Real time listener
        galleryViewModel.listenToSets()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
