package com.davygeeroms.dartsgames.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.davygeeroms.dartsgames.entities.*
import com.davygeeroms.dartsgames.enums.BoardValues
import com.davygeeroms.dartsgames.enums.CheckOutTable
import com.davygeeroms.dartsgames.factories.BoardValueFactory
import com.davygeeroms.dartsgames.interfaces.GameType
import com.davygeeroms.dartsgames.persistence.GameDao
import com.davygeeroms.dartsgames.repositories.GameRepository
import kotlinx.coroutines.*

/**
 * ViewModel corresponding with the PlayGame fragment
 */
class PlayGameViewModel(application: Application, gameDao: GameDao) : AndroidViewModel(application) {

    //Coroutine
    private var vmJob = Job()
    private var uiScope = CoroutineScope(Dispatchers.Main + vmJob)
    private val _gameRepository = GameRepository(gameDao)

    private var _currentGame: MutableLiveData<Game> = MutableLiveData()
    val currentGame : LiveData<Game>
        get() = _currentGame

    private var _checkOutTable: MutableLiveData<CheckOutTable?> = MutableLiveData()
    val checkOutTable : LiveData<CheckOutTable?>
        get() = _checkOutTable

    private var _previousThrow: MutableLiveData<Turn?> = MutableLiveData()
    val previousThrow : LiveData<Turn?>
        get() = _previousThrow

    private val boardValueFactory =  BoardValueFactory()

    /**
     * Continues specific game
     * @param gameId: Id of game to be continued
     */
    fun continueGame(gameId: Int){
        getSavedGame(gameId)
    }

    /**
     * In case of a newly made game, update the newGame flag to false. This game is now ongoing.
     */
    fun updateNewGameStatus(){
        _currentGame.value?.newGame = false
        saveGame()
    }

    /**
     * Saves the current ongoing game.
     */
    fun saveGame(){
        saveGame(_currentGame.value!!)
    }

    /**
     * Player throws dart, update all relevant fields.
     * @param boardValue: String representing dart
     */
    fun throwDart(boardValue: String){

        _previousThrow.value = _currentGame.value?.currentTurn
        _currentGame.value?.throwDart(boardValueFactory.getBoardValue(BoardValues.valueOf(boardValue)))
        _currentGame.postValue(_currentGame.value)
        updateCheckOutTable()
    }

    /**
     * Change the checkout table if needed, based on current score.
     */
    fun updateCheckOutTable(){

        if(_currentGame.value?.gameType?.checkOutTable == true){
            _checkOutTable.postValue(CheckOutTable.valueOf(_currentGame.value?.currentTurn?.playerScore?.score))
        }
    }

    /**
     * Undoes the throw and sets scores back to the throw before
     */
    fun undoLastThrow(){
        _currentGame.value?.undoThrow(_previousThrow.value!!)
        _previousThrow.value = null
        _currentGame.postValue(_currentGame.value)
        updateCheckOutTable()
    }

    /**
     * Fetch specific game from database
     * @param gameId: Id of the game to be fetched.
     */
    private fun getSavedGame(gameId:Int){
        uiScope.launch {
            withContext(Dispatchers.IO){
                val currGame = _gameRepository.getSavedGameById(gameId)
                _currentGame.postValue(currGame)
            }
        }
    }

    /**
     * Saves current game
     * @param game: Game to save
     */
    private fun saveGame(game: Game){
        uiScope.launch {
            withContext(Dispatchers.IO){
                _gameRepository.saveGame(game)
            }
        }
    }
}