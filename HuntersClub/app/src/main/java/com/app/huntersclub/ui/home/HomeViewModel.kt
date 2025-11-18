package com.app.huntersclub.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.huntersclub.data.MonsterDAO
import com.app.huntersclub.model.Monster

class HomeViewModel(private val dao: MonsterDAO) : ViewModel() {

    private val _monsters = MutableLiveData<List<Monster>>()
    val monsters: LiveData<List<Monster>> = _monsters

    fun loadMonsters() {
        val list = dao.getAllMonsters()
        _monsters.value = list
    }
}
