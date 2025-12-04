package com.app.huntersclub.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.navigation.fragment.navArgs
import com.app.huntersclub.data.MonsterDAO
import com.app.huntersclub.data.MyDatabaseHelper
import com.app.huntersclub.databinding.FragmentMonsterDataBinding
import com.app.huntersclub.model.MonsterData
import com.bumptech.glide.Glide

class MonsterDetailFragment : Fragment() {

    private var _binding: FragmentMonsterDataBinding? = null
    private val binding get() = _binding!!

    //We use SafeArgs to implement traveling from list to monster data and vice versa
    private val args: MonsterDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMonsterDataBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val monsterId = args.monsterId

        val dbHelper = MyDatabaseHelper(requireContext())
        dbHelper.createDatabase()
        val dao = MonsterDAO(dbHelper)
        //We get the specific monster data selected in the previous view
        val monster: MonsterData? = dao.getMonsterById(monsterId)

        monster?.let {
            binding.monsterName.text = it.name
            binding.monsterEcology.text = it.monCategory
            binding.monsterDescription.text = it.description

            //Load image from app\src\main\assets\monsters using Glide to
            val imagePath = "file:///android_asset/monsters/${it.id}.png"

            Glide.with(requireContext())
                .load(imagePath)
                .into(binding.monsterImage)

            //Weaknesses and their alternative (not all monsters have alternative weakness)
            val weaknesses = listOf(
                Triple("fire", it.weaknessFire, it.altWeaknessFire),
                Triple("water", it.weaknessWater, it.altWeaknessWater),
                Triple("thunder", it.weaknessThunder, it.altWeaknessThunder),
                Triple("ice", it.weaknessIce, it.altWeaknessIce),
                Triple("dragon", it.weaknessDragon, it.altWeaknessDragon),
                Triple("poison", it.weaknessPoison, it.altWeaknessPoison),
                Triple("sleep", it.weaknessSleep, it.altWeaknessSleep),
                Triple("paralysis", it.weaknessParalysis, it.altWeaknessParalysis),
                Triple("blast", it.weaknessBlast, it.altWeaknessBlast),
                Triple("stun", it.weaknessStun, it.altWeaknessStun)
            )

            binding.weaknessRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            binding.weaknessRecyclerView.adapter = WeaknessAdapter(weaknesses, it.hasAltWeakness)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
