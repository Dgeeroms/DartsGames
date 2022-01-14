package com.davygeeroms.dartsgames.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.davygeeroms.dartsgames.entities.Game
import com.davygeeroms.dartsgames.persistence.GameDao
import com.davygeeroms.dartsgames.repositories.GameRepository
import kotlinx.coroutines.*

/**
 * ViewModel corresponding with Winner fragment. Displays winner and statistics for all players.
 */
class WinnerViewModel(application: Application, gameDao: GameDao) : AndroidViewModel(application) {

    //Coroutine
    private var vmJob = Job()
    private var uiScope = CoroutineScope(Dispatchers.Main + vmJob)
    private val _gameRepository = GameRepository(gameDao)

    private var _currentGame : MutableLiveData<Game> = MutableLiveData()
    val currentGame : LiveData<Game>
        get() = _currentGame

    /**
     * Fetch a specific saved game from the database.
     * @param gameId: Id of the saved game that needs to be fetched.
     */
    fun getFinalizedGame(gameId: Int){
        getSavedGame(gameId)
    }

    /**
     * Fetch a specific saved game from the database.
     * @param gameId: Id of the saved game that needs to be fetched.
     */
    private fun getSavedGame(gameId:Int){
        uiScope.launch {
            withContext(Dispatchers.IO){
                val currGame = _gameRepository.getSavedGameById(gameId)
                _currentGame.postValue(currGame)
            }

        }
    }



}