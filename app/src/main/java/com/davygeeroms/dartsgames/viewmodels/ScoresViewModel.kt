package com.davygeeroms.dartsgames.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.davygeeroms.dartsgames.entities.Game
import com.davygeeroms.dartsgames.persistence.GameDao
import com.davygeeroms.dartsgames.repositories.GameRepository
import kotlinx.coroutines.*

/**
 * ViewModel corresponding with the Scores fragment
 */
class ScoresViewModel(application: Application, gameDao: GameDao) : AndroidViewModel(application){

    //Coroutine
    private var vmJob = Job()
    private var uiScope = CoroutineScope(Dispatchers.Main + vmJob)

    //repo
    private val _gameRepository = GameRepository(gameDao)

    //list of ongoing games
    private var _gameList: MutableLiveData<List<Game>> = MutableLiveData()
    val gameList : LiveData<List<Game>>
        get() = _gameList

    init {
        getSavedEndedGames()
    }

    /**
     * Deletes a specific game
     * @param id: Id of the game to be deleted
     */
    fun deleteGame(id: Int){
        uiScope.launch {
            withContext(Dispatchers.IO){
                _gameRepository.deleteSavedGame(id)
                _gameList.postValue(_gameRepository.getSavedOngoingGames())
            }
        }
    }

    /**
     * Fetches list of ended games from the database.
     */
    private fun getSavedEndedGames(){
        uiScope.launch {
            withContext(Dispatchers.IO){
                _gameList.postValue(_gameRepository.getSavedEndedGames())
            }
        }
    }
}