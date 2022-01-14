package com.davygeeroms.dartsgames.entities

import com.davygeeroms.dartsgames.enums.BoardValues
import com.davygeeroms.dartsgames.enums.GameModes
import com.davygeeroms.dartsgames.factories.BoardValueFactory
import com.davygeeroms.dartsgames.factories.GameTypeFactory
import junit.framework.TestCase
import org.junit.Assert
import org.junit.Before

class GameTest : TestCase() {

    lateinit var testGame: Game
    lateinit var play1: Player
    lateinit var play2: Player
    var playerScores: MutableList<PlayerScore> = mutableListOf()

    @Before
    override fun setUp() {
        super.setUp()

        val gtf = GameTypeFactory()

        play1 = Player(1, "Player 1", "#FF0000")
        play2 = Player(2, "Player 2", "#FF0000")
        playerScores.add(PlayerScore(play1, 501, Statistic()))
        playerScores.add(PlayerScore(play2, 501, Statistic()))

        testGame = Game(gameType = gtf.getGameType(GameModes.FIVEHUNDREDANDONE), playerScores = playerScores, newGame = false)
        testGame.newGame()
    }

    fun testThrowDart() {

        val thrownDart = BoardValues.T20 //60
        val bvf = BoardValueFactory()

        testGame.throwDart(bvf.getBoardValue(thrownDart))

        Assert.assertEquals(testGame.currentTurn.playerScore.score, 441)

    }


}