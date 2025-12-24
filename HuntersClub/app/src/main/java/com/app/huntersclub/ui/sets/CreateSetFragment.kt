package com.app.huntersclub.ui.sets

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
import com.app.huntersclub.model.Weapon
import com.app.huntersclub.model.Decoration
import com.app.huntersclub.utils.ImagePath
import com.bumptech.glide.Glide
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
        //Initializing the texts as before adding the images
        //To have persistency after selecting one piece of the set
        //And the images not returning to default after selecting another
        //Piece of the set
        viewModel.selectedWeapon?.let {
            binding.txtWeapon.text = viewModel.selectedWeapon?.name
            Glide.with(this)
                .load(ImagePath.getAssetPath("weapons", it.rarity, it.weaponType))
                .placeholder(R.drawable.gs)
                .into(binding.imgWeapon)
        } ?: run { binding.txtWeapon.text = "Seleccionar Arma" }

        viewModel.selectedHead?.let {
            binding.txtHead.text = viewModel.selectedHead?.name
            Glide.with(this)
                .load(ImagePath.getAssetPath("armor", it.rarity, "head"))
                .placeholder(R.drawable.head)
                .into(binding.imgHead)
        } ?: run { binding.txtHead.text = "Seleccionar Cabeza" }

        viewModel.selectedChest?.let {
            binding.txtChest.text = viewModel.selectedChest?.name
            Glide.with(this)
                .load(ImagePath.getAssetPath("armor", it.rarity, "chest"))
                .placeholder(R.drawable.chest)
                .into(binding.imgChest)
        } ?: run { binding.txtChest.text = "Seleccionar Torso" }

        viewModel.selectedArms?.let {
            binding.txtArms.text = viewModel.selectedArms?.name
            Glide.with(this)
                .load(ImagePath.getAssetPath("armor", it.rarity, "arms"))
                .placeholder(R.drawable.arms)
                .into(binding.imgArms)
        } ?: run { binding.txtArms.text = "Seleccionar Brazos" }

        viewModel.selectedWaist?.let {
            binding.txtWaist.text = viewModel.selectedWaist?.name
            Glide.with(this)
                .load(ImagePath.getAssetPath("armor", it.rarity, "waist"))
                .placeholder(R.drawable.waist) .into(binding.imgWaist)
        } ?: run { binding.txtWaist.text = "Seleccionar Cintura" }

        viewModel.selectedLegs?.let {
            binding.txtLegs.text = viewModel.selectedLegs?.name
            Glide.with(this)
                .load(ImagePath.getAssetPath("armor", it.rarity, "legs"))
                .placeholder(R.drawable.legs) .into(binding.imgLegs)
        } ?: run { binding.txtLegs.text = "Seleccionar Piernas" }

        viewModel.selectedCharm?.let {
            binding.txtCharm.text = viewModel.selectedCharm?.name
            Glide.with(this)
                .load(ImagePath.getAssetPath("charms", it.rarity))
                .placeholder(R.drawable.charm) .into(binding.imgCharm)
        } ?: run { binding.txtCharm.text = "Seleccionar Cigua" }

        //getParcelable is depreciated but we need it since minimum API is 23.
        //Originally, minimum API was 21 but we increase it to 23
        //To work properly with Firebase
        setFragmentResultListener("weaponSelection") { _, bundle ->
            val weapon: Weapon? = bundle.getParcelable("selectedWeapon")
            viewModel.selectedWeapon = weapon
            binding.txtWeapon.text = weapon?.name ?: "Seleccionar Arma"

            weapon?.let {
                Glide.with(this)
                    .load(ImagePath.getAssetPath("weapons", it.rarity, it.weaponType))
                    .placeholder(R.drawable.gs)
                    .into(binding.imgWeapon) }
        }

        setFragmentResultListener("armorSelection") { _, bundle ->
            val armorType = bundle.getString("armorType")
            val armor: Armor? = bundle.getParcelable("selectedArmor")

            when (armorType) {
                "head" -> {
                    viewModel.selectedHead = armor
                    binding.txtHead.text = armor?.name ?: "Seleccionar Cabeza"
                    armor?.let {
                        Glide.with(this)
                            .load(ImagePath.getAssetPath("armor", it.rarity, "head"))
                            .placeholder(R.drawable.head)
                            .into(binding.imgHead) }
                }
                "chest" -> {
                    viewModel.selectedChest = armor
                    binding.txtChest.text = armor?.name ?: "Seleccionar Torso"
                    armor?.let {
                        Glide.with(this)
                            .load(ImagePath.getAssetPath("armor", it.rarity, "chest"))
                            .placeholder(R.drawable.chest)
                            .into(binding.imgChest) }
                }
                "arms" -> {
                    viewModel.selectedArms = armor
                    binding.txtArms.text = armor?.name ?: "Seleccionar Brazos"
                    armor?.let {
                        Glide.with(this)
                            .load(ImagePath.getAssetPath("armor", it.rarity, "arms"))
                            .placeholder(R.drawable.arms)
                            .into(binding.imgArms) }
                }
                "waist" -> {
                    viewModel.selectedWaist = armor
                    binding.txtWaist.text = armor?.name ?: "Seleccionar Cintura"
                    armor?.let {
                        Glide.with(this)
                            .load(ImagePath.getAssetPath("armor", it.rarity, "waist"))
                            .placeholder(R.drawable.waist)
                            .into(binding.imgWaist) }
                }
                "legs" -> {
                    viewModel.selectedLegs = armor
                    binding.txtLegs.text = armor?.name ?: "Seleccionar Piernas"
                    armor?.let {
                        Glide.with(this)
                            .load(ImagePath.getAssetPath("armor", it.rarity, "legs"))
                            .placeholder(R.drawable.legs)
                            .into(binding.imgLegs) }
                }
            }
        }
        setFragmentResultListener("charmSelection") { _, bundle ->
            val charm: Charm? = bundle.getParcelable("selectedCharm")
            viewModel.selectedCharm = charm
            binding.txtCharm.text = charm?.name ?: "Seleccionar Cigua"
            charm?.let {
                Glide.with(this)
                    .load(ImagePath.getAssetPath("charms", it.rarity))
                    .placeholder(R.drawable.charm)
                    .into(binding.imgCharm)
            }
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
