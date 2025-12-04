package com.app.huntersclub.ui.gallery

import androidx.lifecycle.ViewModel
import com.app.huntersclub.model.Armor
import com.app.huntersclub.model.Weapon
import com.app.huntersclub.model.Charm
import com.app.huntersclub.model.Decoration

class CreateSetViewModel : ViewModel() {
    var selectedWeapon: Weapon? = null
    var selectedHead: Armor? = null
    var selectedChest: Armor? = null
    var selectedArms: Armor? = null
    var selectedWaist: Armor? = null
    var selectedLegs: Armor? = null
    var selectedCharm: Charm? = null

    val selectedDecorations: MutableMap<String, List<Decoration?>> = mutableMapOf()
    //Function to reset the selected pieces of a set
    //When leaving the creation of a set
    fun resetSelections() {
        selectedWeapon = null
        selectedHead = null
        selectedChest = null
        selectedArms = null
        selectedWaist = null
        selectedLegs = null
        selectedCharm = null
        selectedDecorations.clear()
    }

}


