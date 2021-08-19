package com.davygeeroms.dartsgames.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.davygeeroms.dartsgames.entities.Player
import com.davygeeroms.dartsgames.enums.GameModes
import com.davygeeroms.dartsgames.factories.GameTypeFactory
import com.davygeeroms.dartsgames.interfaces.GameType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class NewGameViewModel(application: Application) : AndroidViewModel(application) {

    //Coroutine
    private var vmJob = Job()
    private var uiScope = CoroutineScope(Dispatchers.Main + vmJob)

    //Data
    private var _players: MutableLiveData<MutableList<Player>> = MutableLiveData()
    val players : LiveData<MutableList<Player>>
       get() = _players

    init {
        _players.value = mutableListOf()
    }

    lateinit var selectedGameType : GameType

    private fun setSelectedGameType(gm: GameModes){
        val gtf = GameTypeFactory()
        selectedGameType = gtf.getGameType(gm)
    }

    fun addPlayer(player: Player) {
        _players.value?.add(player)
        _players.postValue(_players.value)
    }

    fun removePlayer(player: Player){
        _players.value?.remove(player)
        _players.postValue(_players.value)
    }

    private fun reindexPlayers(){
        if(_players.value?.count()!! > 0){
            var i = 1
            while (i <= 1){
                _players.value?.get(i - 1)?.number = i
                i++
            }
        }
    }

    fun startGame(gameModes: GameModes){

        setSelectedGameType(gameModes)
        reindexPlayers()

    }
}
