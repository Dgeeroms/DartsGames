package com.davygeeroms.dartsgames.entities.gametypes

import com.davygeeroms.dartsgames.entities.BoardValue
import com.davygeeroms.dartsgames.entities.Turn
import com.davygeeroms.dartsgames.enums.BoardValues
import com.davygeeroms.dartsgames.factories.BoardValueFactory
import com.davygeeroms.dartsgames.interfaces.GameType

class GTClockSingle() : GameType {

    override val description = "AROUNDTHECLOCK"
    override val startScore: Int = 20
    override val targetScore: Int = 0
    override val winModifier: Int = 1
    override val dartsAmount: Int = 3
    override val checkOutTable: Boolean = false

    override fun hasWon(currentScore: Int, dartThrow: BoardValue): Boolean {

        if(currentScore == targetScore && dartThrow.modifier == winModifier){
            return true
        }
        return false
    }

    override fun wasHit(currentScore: Int, dartThrow: BoardValue): Boolean {
        return currentScore - dartThrow.value == 0
    }

    override fun calcScore(currentScore: Int, dartThrow: BoardValue) : Int {
        if(currentScore - dartThrow.value == 0){
            return currentScore - 1
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