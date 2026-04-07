package com.app.huntersclub.ui.sets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.BundleCompat
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
import com.app.huntersclub.utils.DecoDrawableCache.loadDecorationDrawable
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
        hideAllDecoButtons()
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
        } ?: run { binding.txtWeapon.text = getString(R.string.seleccionar_arma) }

        viewModel.selectedHead?.let {
            binding.txtHead.text = viewModel.selectedHead?.name
            Glide.with(this)
                .load(ImagePath.getAssetPath("armor", it.rarity, "head"))
                .placeholder(R.drawable.head)
                .into(binding.imgHead)
        } ?: run { binding.txtHead.text = getString(R.string.seleccionar_cabeza) }

        viewModel.selectedChest?.let {
            binding.txtChest.text = viewModel.selectedChest?.name
            Glide.with(this)
                .load(ImagePath.getAssetPath("armor", it.rarity, "chest"))
                .placeholder(R.drawable.chest)
                .into(binding.imgChest)
        } ?: run { binding.txtChest.text = getString(R.string.seleccionar_torso) }

        viewModel.selectedArms?.let {
            binding.txtArms.text = viewModel.selectedArms?.name
            Glide.with(this)
                .load(ImagePath.getAssetPath("armor", it.rarity, "arms"))
                .placeholder(R.drawable.arms)
                .into(binding.imgArms)
        } ?: run { binding.txtArms.text = getString(R.string.seleccionar_brazos) }

        viewModel.selectedWaist?.let {
            binding.txtWaist.text = viewModel.selectedWaist?.name
            Glide.with(this)
                .load(ImagePath.getAssetPath("armor", it.rarity, "waist"))
                .placeholder(R.drawable.waist)
                .into(binding.imgWaist)
        } ?: run { binding.txtWaist.text = getString(R.string.seleccionar_cintura) }

        viewModel.selectedLegs?.let {
            binding.txtLegs.text = viewModel.selectedLegs?.name
            Glide.with(this)
                .load(ImagePath.getAssetPath("armor", it.rarity, "legs"))
                .placeholder(R.drawable.legs)
                .into(binding.imgLegs)
        } ?: run { binding.txtLegs.text = getString(R.string.seleccionar_piernas) }

        viewModel.selectedCharm?.let {
            binding.txtCharm.text = viewModel.selectedCharm?.name
            Glide.with(this)
                .load(ImagePath.getAssetPath("charms", it.rarity))
                .placeholder(R.drawable.charm)
                .into(binding.imgCharm)
        } ?: run { binding.txtCharm.text = getString(R.string.seleccionar_cigua) }


        //Now we manage the buttons of the decorations in the listeners
        setFragmentResultListener("weaponSelection") { _, bundle ->
            val weapon = BundleCompat.getParcelable(bundle, "selectedWeapon", Weapon::class.java)
            viewModel.clearDecorationsForPiece("weapon")
            viewModel.selectedWeapon = weapon
            binding.txtWeapon.text = weapon?.name ?: "Seleccionar Arma"

            weapon?.let {
                Glide.with(this)
                    .load(ImagePath.getAssetPath("weapons", it.rarity, it.weaponType))
                    .placeholder(R.drawable.gs)
                    .into(binding.imgWeapon)
                updateWeaponSlotsUI(it)
            }
        }

        setFragmentResultListener("armorSelection") { _, bundle ->
            val armorType = bundle.getString("armorType")
            val armor = BundleCompat.getParcelable(bundle, "selectedArmor", Armor::class.java)

            when (armorType) {
                "head" -> {
                    viewModel.clearDecorationsForPiece("head")
                    viewModel.selectedHead = armor
                    binding.txtHead.text = armor?.name ?: "Seleccionar Cabeza"
                    armor?.let {
                        Glide.with(this)
                            .load(ImagePath.getAssetPath("armor", it.rarity, "head"))
                            .placeholder(R.drawable.head)
                            .into(binding.imgHead)
                        updateArmorSlotsUI(it, "head")
                    }
                }
                "chest" -> {
                    viewModel.clearDecorationsForPiece("chest")
                    viewModel.selectedChest = armor
                    binding.txtChest.text = armor?.name ?: "Seleccionar Torso"
                    armor?.let {
                        Glide.with(this)
                            .load(ImagePath.getAssetPath("armor", it.rarity, "chest"))
                            .placeholder(R.drawable.chest)
                            .into(binding.imgChest)
                        updateArmorSlotsUI(it, "chest")
                    }
                }
                "arms" -> {
                    viewModel.clearDecorationsForPiece("arms")
                    viewModel.selectedArms = armor
                    binding.txtArms.text = armor?.name ?: "Seleccionar Brazos"
                    armor?.let {
                        Glide.with(this)
                            .load(ImagePath.getAssetPath("armor", it.rarity, "arms"))
                            .placeholder(R.drawable.arms)
                            .into(binding.imgArms)
                        updateArmorSlotsUI(it, "arms")
                    }
                }
                "waist" -> {
                    viewModel.clearDecorationsForPiece("waist")
                    viewModel.selectedWaist = armor
                    binding.txtWaist.text = armor?.name ?: "Seleccionar Cintura"
                    armor?.let {
                        Glide.with(this)
                            .load(ImagePath.getAssetPath("armor", it.rarity, "waist"))
                            .placeholder(R.drawable.waist)
                            .into(binding.imgWaist)
                        updateArmorSlotsUI(it, "waist")
                    }
                }
                "legs" -> {
                    viewModel.clearDecorationsForPiece("legs")
                    viewModel.selectedLegs = armor
                    binding.txtLegs.text = armor?.name ?: "Seleccionar Piernas"
                    armor?.let {
                        Glide.with(this)
                            .load(ImagePath.getAssetPath("armor", it.rarity, "legs"))
                            .placeholder(R.drawable.legs)
                            .into(binding.imgLegs)
                        updateArmorSlotsUI(it, "legs")
                    }
                }
            }
        }

        setFragmentResultListener("charmSelection") { _, bundle ->
            val charm = BundleCompat.getParcelable(bundle, "selectedCharm", Charm::class.java)
            viewModel.selectedCharm = charm
            binding.txtCharm.text = charm?.name ?: "Seleccionar Cigua"
            charm?.let {
                Glide.with(this)
                    .load(ImagePath.getAssetPath("charms", it.rarity))
                    .placeholder(R.drawable.charm)
                    .into(binding.imgCharm)
            }
        }

        setFragmentResultListener("decoSelection") { _, bundle ->
            val deco = BundleCompat.getParcelable(bundle, "selectedDeco", Decoration::class.java)
            val piece = bundle.getString("piece")!!
            val slotIndex = bundle.getInt("slotIndex")

            viewModel.setDecoration(piece, slotIndex, deco)

            // Update ONLY this slot
            if (piece == "weapon") {
                updateWeaponSlotsUI(viewModel.selectedWeapon!!)
            } else {
                val armor = when(piece) {
                    "head" -> viewModel.selectedHead
                    "chest" -> viewModel.selectedChest
                    "arms" -> viewModel.selectedArms
                    "waist" -> viewModel.selectedWaist
                    "legs" -> viewModel.selectedLegs
                    else -> null
                }
                armor?.let { updateArmorSlotsUI(it, piece) }
            }
        }



        //Buttons for the set
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
        //We add a condition to prevent names like
        //" "
        //" a"
        //" a "
        //"a "
        binding.btnSaveSet.setOnClickListener {
            val db = FirebaseFirestore.getInstance()
            val userId = FirebaseAuth.getInstance().currentUser!!.uid
            viewModel.setName = binding.txtName.text.toString()
            val name = viewModel.setName.trim()
            //Now sets will have a name
            if (name.isEmpty()){
                Toast.makeText(context, "El set debe tener un nombre.", Toast.LENGTH_SHORT).show()
            }else{
                //Create Set as HashMap, saving the userId on the set
                val newSet = hashMapOf(
                    "name" to viewModel.setName,
                    "weapon" to viewModel.selectedWeapon?.id,
                    "head" to viewModel.selectedHead?.id,
                    "torso" to viewModel.selectedChest?.id,
                    "arms" to viewModel.selectedArms?.id,
                    "waist" to viewModel.selectedWaist?.id,
                    "legs" to viewModel.selectedLegs?.id,
                    "charm" to viewModel.selectedCharm?.id,
                    "decorations" to viewModel.selectedDecorations.mapValues {
                        it.value?.id },
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

        viewModel.selectedWeapon?.let {
            updateWeaponSlotsUI(it)
        }
        viewModel.selectedHead?.let {
            updateArmorSlotsUI(it, "head")
        }
        viewModel.selectedChest?.let {
            updateArmorSlotsUI(it, "chest")
        }
        viewModel.selectedArms?.let {
            updateArmorSlotsUI(it, "arms")
        }
        viewModel.selectedWaist?.let {
            updateArmorSlotsUI(it, "waist")
        }
        viewModel.selectedLegs?.let {
            updateArmorSlotsUI(it, "legs")
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
    //Aux functions to work with the buttons
    private fun hideAllDecoButtons() {
        val allButtons = listOf(
            binding.btnWeapon1, binding.btnWeapon2,
            binding.btnHead1, binding.btnHead2, binding.btnHead3,
            binding.btnChest1, binding.btnChest2, binding.btnChest3,
            binding.btnArms1, binding.btnArms2, binding.btnArms3,
            binding.btnWaist1, binding.btnWaist2, binding.btnWaist3,
            binding.btnLegs1, binding.btnLegs2, binding.btnLegs3
        )
        allButtons.forEach {
            it.visibility = View.GONE
        }
    }

    private fun setupDecoButton(
        button: View,
        piece: String,
        slotIndex: Int,
        slotSize: Int
    ) {
        button.setOnClickListener {
            val action = CreateSetFragmentDirections
                .actionCreateSetFragmentToSelectDecoFragment(
                    slotSize = slotSize,
                    piece = piece,
                    slotIndex = slotIndex
                )
            findNavController().navigate(action)
        }
    }

    private fun updateWeaponSlotsUI(weapon: Weapon) {
        val buttons = listOf(binding.btnWeapon1, binding.btnWeapon2)
        val slots = listOf(weapon.slot1, weapon.slot2, weapon.slot3)

        buttons.forEach { it.visibility = View.GONE }

        slots.forEachIndexed { index, slotSize ->
            if (slotSize > 0) {
                val button = buttons[index]
                button.visibility = View.VISIBLE

                val deco = viewModel.getDecoration("weapon", index +1)

                val drawable =
                    if (deco != null)
                        loadDecorationDrawable(requireContext(), deco.slot, deco.colour)
                    else
                        loadDecorationDrawable(requireContext(), slotSize, "black")

                button.setImageDrawable(drawable)

                setupDecoButton(button, "weapon", index + 1, slotSize)
            }
        }
    }

    private fun updateArmorSlotsUI(armor: Armor, piece: String) {
        val buttons = when(piece) {
            "head" -> listOf(binding.btnHead1, binding.btnHead2, binding.btnHead3)
            "chest" -> listOf(binding.btnChest1, binding.btnChest2, binding.btnChest3)
            "arms" -> listOf(binding.btnArms1, binding.btnArms2, binding.btnArms3)
            "waist" -> listOf(binding.btnWaist1, binding.btnWaist2, binding.btnWaist3)
            "legs" -> listOf(binding.btnLegs1, binding.btnLegs2, binding.btnLegs3)
            else -> return
        }

        val slots = listOf(armor.slot1, armor.slot2, armor.slot3)

        buttons.forEach { it.visibility = View.GONE }

        slots.forEachIndexed { index, slotSize ->
            if (slotSize > 0) {
                val button = buttons[index]
                button.visibility = View.VISIBLE

                val deco = viewModel.getDecoration(piece, index +1)

                val drawable =
                    if (deco != null)
                        loadDecorationDrawable(requireContext(), deco.slot, deco.colour)
                    else
                        loadDecorationDrawable(requireContext(), slotSize, "black")

                button.setImageDrawable(drawable)

                setupDecoButton(button, piece, index + 1, slotSize)
            }
        }
    }

}
