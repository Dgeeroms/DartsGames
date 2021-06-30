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
    private var _players: MutableList<Player> = mutableListOf()
    val players : List<Player>
       get() = _players

    lateinit var selectedGameType : GameType

    fun setSelectedGameType(gm: GameModes){
        val gtf = GameTypeFactory()
        selectedGameType = gtf.getGameType(gm)
    }

    fun addPlayer(player: Player){
        _players.add(player)
    }

    fun removePlayer(player: Player){
        _players.remove(player)
    }
}
