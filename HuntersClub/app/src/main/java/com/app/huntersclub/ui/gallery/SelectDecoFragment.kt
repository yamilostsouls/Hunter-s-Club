package com.app.huntersclub.ui.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.fragment.app.setFragmentResult
import com.app.huntersclub.R
import com.app.huntersclub.data.DecoDAO
import com.app.huntersclub.data.MyDatabaseHelper

class SelectDecoFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var decoDao: DecoDAO

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.select_decoration, container, false)

        recyclerView = view.findViewById(R.id.recyclerViewDecoration)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        decoDao = DecoDAO(MyDatabaseHelper(requireContext()))

        val allDecorations = decoDao.getAllDecorations()
        val slotSize = arguments?.getInt("slotSize") ?: 4
        val availableDecorations = allDecorations.filter { it.slot <= slotSize }


        val adapter = DecoAdapter(availableDecorations) { selectedDeco ->
            val result = Bundle().apply {
                putParcelable("selectedDeco", selectedDeco)
                putInt("slotSize", slotSize)
            }
            setFragmentResult("decoSelection", result)
            parentFragmentManager.popBackStack()
        }

        recyclerView.adapter = adapter

        return view
    }
}
