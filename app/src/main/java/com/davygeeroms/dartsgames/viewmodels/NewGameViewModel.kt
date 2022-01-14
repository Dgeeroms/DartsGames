package com.davygeeroms.dartsgames.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.davygeeroms.dartsgames.entities.Game
import com.davygeeroms.dartsgames.entities.Player
import com.davygeeroms.dartsgames.entities.PlayerScore
import com.davygeeroms.dartsgames.entities.Statistic
import com.davygeeroms.dartsgames.enums.GameModes
import com.davygeeroms.dartsgames.factories.GameTypeFactory
import com.davygeeroms.dartsgames.interfaces.GameType
import com.davygeeroms.dartsgames.persistence.GameDao
import com.davygeeroms.dartsgames.repositories.GameRepository
import kotlinx.coroutines.*

/**
 * ViewModel for NewGame fragment
 */
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

    /**
     * Sets the selected game type for this new game
     * @param gm: Enum for game modes
     */
    private fun setSelectedGameType(gm: GameModes){
        val gtf = GameTypeFactory()
        selectedGameType = gtf.getGameType(gm)
    }

    /**
     * Adds a player to the new game
     * @param player: Player to be added
     */
    fun addPlayer(player: Player) {
        _players.value?.add(player)
        _players.postValue(_players.value)
    }

    /**
     * Removes a player from the new game
     * @param player: Player to be removed
     */
    fun removePlayer(player: Player){
        _players.value?.remove(player)
        _players.postValue(_players.value)
    }

    /**
     * Every player has a number, if a few were added & removed from random locations,
     * these numbers will nog follow eachother anymore and mess stuff up.
     * This method renumbers every player from 1 to the last player.
     */
    private fun reindexPlayers(){
        if(_players.value?.count()!! > 0){
            var i = 1
            while (i <= _players.value?.count()!!){
                _players.value?.get(i - 1)?.number = i
                i++
            }
        }
    }

    /**
     * Starts the game.
     * @param gameModes: Enum for selected game mode
     */
    fun startGame(gameModes: GameModes){

        setSelectedGameType(gameModes)
        reindexPlayers()


        val playerScores = mutableListOf<PlayerScore>()

        for (player in _players.value!!){
            playerScores.add(PlayerScore(player, selectedGameType.startScore, Statistic()))
        }

        val newGame = Game(gameType = selectedGameType, playerScores = playerScores, newGame = true)
        newGame.newGame()
        saveGameAndFetchFromDB(newGame)
    }

    /**
     * Saves the game and immediately fetches it from database again
     * This to generate a unique id that can be passed to the next view model
     */
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
