package com.davygeeroms.dartsgames.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.davygeeroms.dartsgames.entities.Game
import com.davygeeroms.dartsgames.persistence.GameDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GameRepository(private val gameDao: GameDao) {
/*
    private var _game = MutableLiveData<Game>()
    val game : LiveData<Game>
        get() = _game

    suspend fun getSavedGames(): List<Game> {
        return withContext(Dispatchers.IO){
            gameDao.getSavedGames()
        }
    }
*/
    suspend fun getSavedOngoingGames(): List<Game> {
        return withContext(Dispatchers.IO){
            gameDao.getSavedOngoingGames()
        }
    }

    suspend fun getSavedEndedGames(): List<Game> {
        return withContext(Dispatchers.IO){
            gameDao.getSavedEndedGames()
        }
    }

    suspend fun getSavedGameById(id: Int): Game {
        return withContext(Dispatchers.IO){
            gameDao.getSavedGameById(id)
        }
    }

    suspend fun getNewGame(): Game {
        return withContext(Dispatchers.IO){
            gameDao.getNewGame()
        }
    }

    suspend fun deleteSavedGame(id: Int) {
        withContext(Dispatchers.IO){
            gameDao.deleteSavedGame(id)
        }
    }

    suspend fun saveGame(game: Game) {
        withContext(Dispatchers.IO){
            gameDao.saveGame(game)
        }
    }

    suspend fun deleteTable() {
        withContext(Dispatchers.IO){
            gameDao.deleteTable()
        }
    }
}