package com.davygeeroms.dartsgames.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.davygeeroms.dartsgames.entities.Game
import com.davygeeroms.dartsgames.persistence.GameDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GameRepository(private val gameDao: GameDao) {

    /**
     * Query database for list of ongoing games.
     * @return List of ongoing games
     */
    suspend fun getSavedOngoingGames(): List<Game> {
        return withContext(Dispatchers.IO){
            gameDao.getSavedOngoingGames()
        }
    }

    /**
     * Query database for list of ended games.
     * @return List of ended games
     */
    suspend fun getSavedEndedGames(): List<Game> {
        return withContext(Dispatchers.IO){
            gameDao.getSavedEndedGames()
        }
    }

    /**
     * Query database for specific game.
     * @param id: Id corresponding with game
     * @return Game
     */
    suspend fun getSavedGameById(id: Int): Game {
        return withContext(Dispatchers.IO){
            gameDao.getSavedGameById(id)
        }
    }

    /**
     * Query database for new game. Game flagged as NewGame = true
     * @return Game
     */
    suspend fun getNewGame(): Game {
        return withContext(Dispatchers.IO){
            gameDao.getNewGame()
        }
    }

    /**
     * Delete a specific game from database
     * @param id: Id of the game to delete
     */
    suspend fun deleteSavedGame(id: Int) {
        withContext(Dispatchers.IO){
            gameDao.deleteSavedGame(id)
        }
    }

    /**
     * Save game.
     * @param game: Game to save
     */
    suspend fun saveGame(game: Game) {
        withContext(Dispatchers.IO){
            gameDao.saveGame(game)
        }
    }

    /**
     * Deletes all entries in savedGames table
     */
    suspend fun deleteTable() {
        withContext(Dispatchers.IO){
            gameDao.deleteTable()
        }
    }
}