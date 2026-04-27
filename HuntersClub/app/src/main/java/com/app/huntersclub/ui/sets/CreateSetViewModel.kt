package com.app.huntersclub.ui.sets

import androidx.lifecycle.ViewModel
import com.app.huntersclub.model.Armor
import com.app.huntersclub.model.Weapon
import com.app.huntersclub.model.Charm
import com.app.huntersclub.model.Decoration

class CreateSetViewModel : ViewModel() {
    var setName: String = ""
    var selectedWeapon: Weapon? = null
    var selectedHead: Armor? = null
    var selectedChest: Armor? = null
    var selectedArms: Armor? = null
    var selectedWaist: Armor? = null
    var selectedLegs: Armor? = null
    var selectedCharm: Charm? = null

    val selectedDecorations: MutableMap<String, Decoration?> = mutableMapOf()

    fun setDecoration(piece: String, slotIndex: Int, decoration: Decoration?) {
        val key = "$piece$slotIndex"
        selectedDecorations[key] = decoration
    }

    fun getDecoration(piece: String, slotIndex: Int): Decoration? {
        val key = "$piece$slotIndex"
        return selectedDecorations[key]
    }
    //Function to reset the selected pieces of a set
    //When leaving the creation of a set
    fun resetSelections() {
        setName = ""
        selectedWeapon = null
        selectedHead = null
        selectedChest = null
        selectedArms = null
        selectedWaist = null
        selectedLegs = null
        selectedCharm = null
        selectedDecorations.clear()
    }

    fun clearDecorationsForPiece(piece: String) {
        selectedDecorations.remove("${piece}1")
        selectedDecorations.remove("${piece}2")
        selectedDecorations.remove("${piece}3")
    }

}


