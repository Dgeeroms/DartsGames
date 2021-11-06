package com.davygeeroms.dartsgames.entities.gametypes

import com.davygeeroms.dartsgames.entities.BoardValue
import com.davygeeroms.dartsgames.entities.Turn
import com.davygeeroms.dartsgames.enums.BoardValues
import com.davygeeroms.dartsgames.factories.BoardValueFactory
import com.davygeeroms.dartsgames.interfaces.GameType
import kotlin.random.Random

class GTRandomBV() : GameType {

    override val description = "RANDOMBOARDVALUES"
    override val targetScore: Int = 0
    override val startScore: Int = 42
    override val winModifier: Int = 1
    override val dartsAmount: Int = 3
    override val checkOutTable: Boolean = false

    override fun hasWon(currentScore: Int, dartThrow: BoardValue): Boolean {
        // random training = endless
        return false
    }

    override fun wasHit(currentScore: Int, dartThrow: BoardValue): Boolean {
        return currentScore - dartThrow.id == 0
    }

    override fun calcScore(currentScore: Int, dartThrow: BoardValue): Int {
        if(currentScore - dartThrow.id == 0){
            return Random.nextInt(1, 62)
        }
        return currentScore
    }

    override fun displayedScoreToString(currentScore: Int): String {

        //CURRENTSCORE is actually your next target in this gamemode

        val bvf = BoardValueFactory()
        val scoreString: String

        if(currentScore == targetScore) {
            scoreString = "End of game"
            return scoreString
        }

        val bv = BoardValues.valueOf(currentScore)
        scoreString = bv?.let { bvf.getBoardValue(it).description }.toString()

        return "Target: $scoreString"

    }

}


