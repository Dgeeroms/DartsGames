package com.davygeeroms.dartsgames.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.davygeeroms.dartsgames.entities.Game
import com.davygeeroms.dartsgames.entities.Player
import com.davygeeroms.dartsgames.entities.PlayerScore
import com.davygeeroms.dartsgames.enums.GameModes
import com.davygeeroms.dartsgames.factories.GameTypeFactory
import com.davygeeroms.dartsgames.interfaces.GameType
import com.davygeeroms.dartsgames.persistence.GameDao
import com.davygeeroms.dartsgames.repositories.GameRepository
import kotlinx.coroutines.*

class NewGameViewModel(application: Application, gameDao: GameDao) : AndroidViewModel(application) {

    //Coroutine
    private var vmJob = Job()
    private var uiScope = CoroutineScope(Dispatchers.Main + vmJob)

    private var _game : MutableLiveData<Game> = MutableLiveData()
    val game : LiveData<Game>
        get() = _game

    //Data
    private var _players: MutableLiveData<MutableList<Player>> = MutableLiveData()
    val players : LiveData<MutableList<Player>>
       get() = _players

    private val _gameRepo = GameRepository(gameDao)

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
            while (i <= _players.value?.count()!!){
                _players.value?.get(i - 1)?.number = i
                i++
            }
        }
    }

    fun startGame(gameModes: GameModes){

        setSelectedGameType(gameModes)
        reindexPlayers()


        val playerScores = mutableListOf<PlayerScore>()

        for (player in _players.value!!){
            playerScores.add(PlayerScore(player, selectedGameType.startScore))
        }

        val newGame = Game(gameType = selectedGameType, playerScores = playerScores, newGame = true)
        newGame.startGame()
        saveGameAndFetchFromDB(newGame)
    }

    private fun saveGameAndFetchFromDB(game: Game){
        uiScope.launch {
            withContext(Dispatchers.IO){

                //_gameRepo.deleteTable()

                _gameRepo.saveGame(game)
                val tempGame = _gameRepo.getNewGame()
                _game.postValue(tempGame)

            }
        }
    }
}
