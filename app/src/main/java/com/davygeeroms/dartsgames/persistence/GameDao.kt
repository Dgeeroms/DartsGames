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

    @Query("SELECT * FROM savedGames WHERE id = :id")
    fun getSavedGameById(id:String): Game

    @Query("DELETE FROM savedGames WHERE id = :id")
    fun deleteSavedGame(id: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveGame(game: Game)


}