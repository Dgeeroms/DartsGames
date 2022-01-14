package com.davygeeroms.dartsgames.entities

import com.davygeeroms.dartsgames.enums.BoardValues
import com.davygeeroms.dartsgames.enums.GameModes
import com.davygeeroms.dartsgames.factories.BoardValueFactory
import com.davygeeroms.dartsgames.factories.GameTypeFactory
import junit.framework.TestCase
import org.junit.Assert
import org.junit.Before
import org.junit.Test

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

    @Test
    fun testThrowDart() {

        val thrownDart = BoardValues.T20 //60
        val bvf = BoardValueFactory()

        testGame.throwDart(bvf.getBoardValue(thrownDart))

        Assert.assertEquals(441, testGame.currentTurn.playerScore.score)

    }

    @Test
    fun testUndoThrow(){

        val thrownDart = BoardValues.T20 //60
        val bvf = BoardValueFactory()

        testGame.throwDart(bvf.getBoardValue(thrownDart))


        val turnToUndo = testGame.currentTurn

        testGame.undoThrow(turnToUndo)

        // NOK > However it works when running the app. Strange.
        Assert.assertEquals(501, testGame.currentTurn.playerScore.score)
    }


    @Test
    fun testPlayerOneWins(){

        var thrownDart = BoardValues.T20 //60
        val bvf = BoardValueFactory()

        //player1
        testGame.throwDart(bvf.getBoardValue(thrownDart)) //60
        testGame.throwDart(bvf.getBoardValue(thrownDart)) //120
        testGame.throwDart(bvf.getBoardValue(thrownDart)) //180

        //player2
        testGame.throwDart(bvf.getBoardValue(thrownDart)) //60
        testGame.throwDart(bvf.getBoardValue(thrownDart)) //120
        testGame.throwDart(bvf.getBoardValue(thrownDart)) //180

        //player1
        testGame.throwDart(bvf.getBoardValue(thrownDart)) //240
        testGame.throwDart(bvf.getBoardValue(thrownDart)) //300
        testGame.throwDart(bvf.getBoardValue(thrownDart)) //360

        //player2
        testGame.throwDart(bvf.getBoardValue(thrownDart)) //240
        testGame.throwDart(bvf.getBoardValue(thrownDart)) //300
        testGame.throwDart(bvf.getBoardValue(thrownDart)) //360

        //player1
        testGame.throwDart(bvf.getBoardValue(thrownDart)) //420
        thrownDart = BoardValues.T19
        testGame.throwDart(bvf.getBoardValue(thrownDart)) //477
        thrownDart = BoardValues.D12
        testGame.throwDart(bvf.getBoardValue(thrownDart)) //501

        Assert.assertEquals(0, testGame.currentTurn.playerScore.score)
        Assert.assertEquals(true, testGame.hasWon)

    }



}