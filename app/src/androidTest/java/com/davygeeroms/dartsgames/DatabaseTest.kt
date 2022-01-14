package com.davygeeroms.dartsgames

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.davygeeroms.dartsgames.entities.Game
import com.davygeeroms.dartsgames.entities.Player
import com.davygeeroms.dartsgames.entities.PlayerScore
import com.davygeeroms.dartsgames.entities.Statistic
import com.davygeeroms.dartsgames.enums.GameModes
import com.davygeeroms.dartsgames.factories.GameTypeFactory
import com.davygeeroms.dartsgames.persistence.AppDatabase
import com.davygeeroms.dartsgames.persistence.GameDao
import com.davygeeroms.dartsgames.repositories.GameRepository
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException


@RunWith(AndroidJUnit4::class)
class DatabaseTest {

    private lateinit var gameDao: GameDao
    private lateinit var db: AppDatabase

    lateinit var testGame: Game
    var testGameId = 0
    lateinit var play1: Player
    lateinit var play2: Player
    var playerScores: MutableList<PlayerScore> = mutableListOf()

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            // Allowing main thread queries, just for testing.
            .allowMainThreadQueries()
            .build()
        gameDao = db.gameDao
    }

    @Before
    fun createGame(){
        val gtf = GameTypeFactory()

        play1 = Player(1, "TESTMEBABY", "#FF0000")
        play2 = Player(2, "Player 2", "#FF0000")
        playerScores.add(PlayerScore(play1, 501, Statistic()))
        playerScores.add(PlayerScore(play2, 501, Statistic()))

        testGame = Game(gameType = gtf.getGameType(GameModes.FIVEHUNDREDANDONE), playerScores = playerScores, newGame = true)
        testGame.newGame()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetGame() {
        gameDao.saveGame(testGame)
        val testGame = gameDao.getNewGame()
        testGame.newGame = false
        testGameId = testGame.id

        assertEquals(testGame.currentTurn.playerScore.player.name, "TESTMEBABY")
    }
    
    @Test
    @Throws(Exception::class)
    fun deleteGame(){
        gameDao.deleteSavedGame(testGameId)
        val shouldBeNull = gameDao.getSavedGameById(testGameId)

        assertNull(shouldBeNull)
    }
}