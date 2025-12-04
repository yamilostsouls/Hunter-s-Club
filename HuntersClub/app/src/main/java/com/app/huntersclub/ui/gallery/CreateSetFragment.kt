package com.app.huntersclub.ui.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import com.app.huntersclub.R
import com.app.huntersclub.databinding.CreateSetBinding
import com.app.huntersclub.model.Armor
import com.app.huntersclub.model.Charm
import com.app.huntersclub.model.Decoration
import com.app.huntersclub.model.Weapon
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CreateSetFragment : Fragment() {

    private var _binding: CreateSetBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CreateSetViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CreateSetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.txtSelectedWeapon.text= viewModel.selectedWeapon?.name
        binding.txtSelectedHead.text = viewModel.selectedHead?.name
        binding.txtSelectedChest.text = viewModel.selectedChest?.name
        binding.txtSelectedArms.text = viewModel.selectedArms?.name
        binding.txtSelectedWaist.text = viewModel.selectedWaist?.name
        binding.txtSelectedLegs.text = viewModel.selectedLegs?.name
        binding.txtSelectedCharm.text = viewModel.selectedCharm?.name

        //getParcelable is depreciated but we need it since minimum API is 23.
        //Originally, minimum API was 21 but we increase it to 23
        //To work properly with Firebase
        setFragmentResultListener("weaponSelection") { _, bundle ->
            val weapon: Weapon? = bundle.getParcelable("selectedWeapon")
            viewModel.selectedWeapon = weapon
            binding.txtSelectedWeapon.text = weapon?.name
        }

        setFragmentResultListener("armorSelection") { _, bundle ->
            val armorType = bundle.getString("armorType")
            val armor: Armor? = bundle.getParcelable("selectedArmor")

            when (armorType) {
                "head" -> {
                    viewModel.selectedHead = armor
                    binding.txtSelectedHead.text = armor?.name
                }
                "chest" -> {
                    viewModel.selectedChest = armor
                    binding.txtSelectedChest.text = armor?.name
                }
                "arms" -> {
                    viewModel.selectedArms = armor
                    binding.txtSelectedArms.text = armor?.name
                }
                "waist" -> {
                    viewModel.selectedWaist = armor
                    binding.txtSelectedWaist.text = armor?.name
                }
                "legs" -> {
                    viewModel.selectedLegs = armor
                    binding.txtSelectedLegs.text = armor?.name
                }
            }
        }
        setFragmentResultListener("charmSelection") { _, bundle ->
            val charm: Charm? = bundle.getParcelable("selectedCharm")
            viewModel.selectedCharm = charm
            binding.txtSelectedCharm.text = charm?.name
        }

        //Buttons for the set
        //Later we will implement the decorations, we have the logic but not implemented
        binding.btnSelectedWeapon.setOnClickListener {
            val action = CreateSetFragmentDirections
                .actionCreateSetFragmentToSelectWeaponFragment()
            findNavController().navigate(action)
        }

        binding.btnSelectedHead.setOnClickListener {
            val action = CreateSetFragmentDirections
                .actionCreateSetFragmentToSelectArmorFragment("head")
            findNavController().navigate(action)
        }

        binding.btnSelectedChest.setOnClickListener {
            val action = CreateSetFragmentDirections
                .actionCreateSetFragmentToSelectArmorFragment("chest")
            findNavController().navigate(action)
        }

        binding.btnSelectedArms.setOnClickListener {
            val action = CreateSetFragmentDirections
                .actionCreateSetFragmentToSelectArmorFragment("arms")
            findNavController().navigate(action)
        }

        binding.btnSelectedWaist.setOnClickListener {
            val action = CreateSetFragmentDirections
                .actionCreateSetFragmentToSelectArmorFragment("waist")
            findNavController().navigate(action)
        }

        binding.btnSelectedLegs.setOnClickListener {
            val action = CreateSetFragmentDirections
                .actionCreateSetFragmentToSelectArmorFragment("legs")
            findNavController().navigate(action)
        }

        binding.btnSelectedCharm.setOnClickListener {
            val action = CreateSetFragmentDirections
                .actionCreateSetFragmentToSelectCharmFragment()
            findNavController().navigate(action)
        }

        //Save button
        binding.btnSaveSet.setOnClickListener {
            val db = FirebaseFirestore.getInstance()
            val userId = FirebaseAuth.getInstance().currentUser!!.uid


            //Create Set as HashMap, saving the userId on the set
            val newSet = hashMapOf(
                "weapon" to (viewModel.selectedWeapon?.id ?: ""),
                "head" to (viewModel.selectedHead?.id ?: ""),
                "torso" to (viewModel.selectedChest?.id ?: ""),
                "arms" to (viewModel.selectedArms?.id ?: ""),
                "waist" to (viewModel.selectedWaist?.id ?: ""),
                "legs" to (viewModel.selectedLegs?.id ?: ""),
                "charm" to (viewModel.selectedCharm?.id ?: ""),
                "userId" to userId
            )

            //Save and store the set in sets collection of Firebase
            db.collection("sets")
                .add(newSet)
                .addOnSuccessListener {
                    Toast.makeText(requireContext(), "Set guardado correctamente", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(requireContext(), "Error al guardar el set", Toast.LENGTH_SHORT).show()
                }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        //Cleans the selected data after returning to sets page
        val navController = findNavController()
        val currentDestination = navController.currentDestination?.id

        if (currentDestination == R.id.nav_gallery) {
            viewModel.resetSelections()
        }
    }
}
