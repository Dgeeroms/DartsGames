package com.davygeeroms.dartsgames.entities.gametypes

import com.davygeeroms.dartsgames.entities.BoardValue
import com.davygeeroms.dartsgames.enums.BoardValues
import com.davygeeroms.dartsgames.enums.CheckOutTable
import com.davygeeroms.dartsgames.factories.BoardValueFactory
import com.davygeeroms.dartsgames.interfaces.GameType
import kotlin.random.Random
import kotlin.reflect.jvm.internal.impl.util.Check

class GTRandomCheckout: GameType {

    override val description = "RANDOMBOARDCHECKOUT"
    override val targetScore: Int = 0
    override val startScore: Int = CheckOutTable.valueOfId(Random.nextInt(1, 162))?.target!!
    override val winModifier: Int = 1
    override val dartsAmount: Int = 3
    override val checkOutTable: Boolean = true

    override fun hasWon(currentScore: Int, dartThrow: BoardValue): Boolean {
        // random training = endless
        return false
    }

    override fun wasHit(currentScore: Int, dartThrow: BoardValue): Boolean {
        return currentScore - dartThrow.id == 0
    }

    override fun calcScore(currentScore: Int, dartThrow: BoardValue): Int? {

        if(currentScore - dartThrow.value * dartThrow.modifier == 0){
            return CheckOutTable.valueOfId(Random.nextInt(1, 162))?.target
        }

        if(currentScore - dartThrow.value * dartThrow.modifier in 1..currentScore){
            return currentScore - dartThrow.value * dartThrow.modifier
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

        scoreString = currentScore.toString()

        return "Target: $scoreString"

    }
}