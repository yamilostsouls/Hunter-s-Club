package com.app.huntersclub.ui.home

import android.graphics.drawable.Drawable
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

        val monster: MonsterData? = dao.getMonsterById(monsterId)

        monster?.let {
            binding.monsterName.text = it.name
            binding.monsterEcology.text = it.monCategory
            binding.monsterDescription.text = it.description

            //Load image from app\src\main\assets\monsters
            try {
                val inputStream = requireContext().assets.open(it.imagenResId)
                val drawable = Drawable.createFromStream(inputStream, null)
                binding.monsterImage.setImageDrawable(drawable)
                inputStream.close()
            } catch (e: Exception) {
                //We have images of all monsters
            }

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
            binding.weaknessRecyclerView.adapter = WeaknessAdapter(weaknesses)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
