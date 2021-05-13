package com.davygeeroms.dartsgames.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.davygeeroms.dartsgames.entities.Player
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class NewGameViewModel(application: Application) : AndroidViewModel(application) {

    //Coroutine
    private var vmJob = Job()
    private var uiScope = CoroutineScope(Dispatchers.Main + vmJob)

    //Data
    private var _players: MutableLiveData<MutableList<Player>> = MutableLiveData(mutableListOf<Player>())
    val players : LiveData<MutableList<Player>>
       get() = _players


    fun addPlayer(player: Player){
        _players.value?.add(player)
        _players.postValue(_players.value)

    }

}
