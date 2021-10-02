package com.davygeeroms.dartsgames.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.davygeeroms.dartsgames.entities.*
import com.davygeeroms.dartsgames.enums.BoardValues
import com.davygeeroms.dartsgames.factories.BoardValueFactory
import com.davygeeroms.dartsgames.interfaces.GameType
import com.davygeeroms.dartsgames.persistence.GameDao
import com.davygeeroms.dartsgames.repositories.GameRepository
import kotlinx.coroutines.*

class PlayGameViewModel(application: Application, gameDao: GameDao) : AndroidViewModel(application) {

    //Coroutine
    private var vmJob = Job()
    private var uiScope = CoroutineScope(Dispatchers.Main + vmJob)
    private val _gameRepository = GameRepository(gameDao)

    private var _currentGame: MutableLiveData<Game> = MutableLiveData()
    val currentGame : LiveData<Game>
        get() = _currentGame


    private var _undoableThrow: MutableLiveData<PlayerScoreHistory?> = MutableLiveData()
    val undoableThrow : LiveData<PlayerScoreHistory?>
        get() = _undoableThrow


    private val boardValueFactory =  BoardValueFactory()


    fun continueGame(gameId: Int){
        getSavedGame(gameId)
    }

    fun updateNewGameStatus(){
        _currentGame.value?.newGame = false
        saveGame(_currentGame.value!!)
    }

    fun throwDart(boardValue: String){

        _currentGame.value?.throwDart(boardValueFactory.getBoardValue(BoardValues.valueOf(boardValue)))
        _currentGame.postValue(_currentGame.value)
        updateUndoableThrow()

    }

    private fun updateUndoableThrow(){
        _undoableThrow.postValue(_currentGame.value?.getLastPlayerScoreHistory())
    }



    fun undoLastThrow(){
        _currentGame.value?.undoThrow(_undoableThrow.value!!)
        _currentGame.postValue(_currentGame.value)
        _undoableThrow.postValue(null)
    }

    private fun getSavedGame(gameId:Int){
        uiScope.launch {
            withContext(Dispatchers.IO){
                val currGame = _gameRepository.getSavedGameById(gameId)
                _currentGame.postValue(currGame)
            }
        }
    }

    private fun saveGame(game: Game){
        uiScope.launch {
            withContext(Dispatchers.IO){
                _gameRepository.saveGame(game)
            }
        }
    }
}