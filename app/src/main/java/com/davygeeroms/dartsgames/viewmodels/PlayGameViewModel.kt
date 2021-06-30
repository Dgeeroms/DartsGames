package com.davygeeroms.dartsgames.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.davygeeroms.dartsgames.entities.Game
import com.davygeeroms.dartsgames.entities.Player
import com.davygeeroms.dartsgames.entities.PlayerScore
import com.davygeeroms.dartsgames.interfaces.GameType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class PlayGameViewModel(application: Application) : AndroidViewModel(application) {

    //Coroutine
    private var vmJob = Job()
    private var uiScope = CoroutineScope(Dispatchers.Main + vmJob)

    private lateinit var _currentGame : Game
    val currentGame : Game
        get() = _currentGame

    fun startGame(gameType: GameType, players: List<Player>){

        var playerScores = mutableListOf<PlayerScore>()

        for (player in players){
            playerScores.add(PlayerScore(player, gameType.startScore))
        }

        _currentGame = Game(gameType, playerScores)
        _currentGame.startGame()

    }
}