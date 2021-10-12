package com.davygeeroms.dartsgames.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.davygeeroms.dartsgames.entities.Game

@Dao
interface GameDao {

    @Query("SELECT * FROM savedGames")
    fun getSavedGames(): List<Game>

    @Query("SELECT * FROM savedGames WHERE has_won = 0")
    fun getSavedOngoingGames(): List<Game>

    @Query("SELECT * FROM savedGames WHERE has_won = 1")
    fun getSavedEndedGames(): List<Game>

    @Query("SELECT * FROM savedGames WHERE game_id = :id")
    fun getSavedGameById(id: Int): Game

    @Query("SELECT * FROM savedGames WHERE new_game = 1")
    fun getNewGame(): Game

    @Query("DELETE FROM savedGames WHERE game_id = :id")
    fun deleteSavedGame(id: Int)

    @Query("DELETE FROM savedGames")
    fun deleteTable()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveGame(game: Game)


}