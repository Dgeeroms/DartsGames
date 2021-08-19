package com.davygeeroms.dartsgames.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.davygeeroms.dartsgames.entities.BoardValue
import com.davygeeroms.dartsgames.entities.Game
import com.davygeeroms.dartsgames.entities.Player
import com.davygeeroms.dartsgames.entities.PlayerScore
import com.davygeeroms.dartsgames.enums.BoardValues
import com.davygeeroms.dartsgames.factories.BoardValueFactory
import com.davygeeroms.dartsgames.interfaces.GameType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class PlayGameViewModel(application: Application) : AndroidViewModel(application) {

    //Coroutine
    private var vmJob = Job()
    private var uiScope = CoroutineScope(Dispatchers.Main + vmJob)

    private var _currentGame : MutableLiveData<Game> = MutableLiveData()
    val currentGame : LiveData<Game>
        get() = _currentGame

    val boardValueFactory =  BoardValueFactory()

    fun startGame(gameType: GameType, players: List<Player>){

        var playerScores = mutableListOf<PlayerScore>()

        for (player in players){
            playerScores.add(PlayerScore(player, gameType.startScore))
        }

        _currentGame.value = Game(gameType, playerScores)
        _currentGame.value!!.startGame()
        _currentGame.postValue(_currentGame.value)

    }

    fun continueGame(game: Game){
        _currentGame.postValue(game)
    }

    fun throwDart(boardValue: String){
        _currentGame.value?.throwDart(boardValueFactory.getBoardValue(BoardValues.valueOf(boardValue)))
        _currentGame.postValue(_currentGame.value)
    }
}