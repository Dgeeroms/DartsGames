package com.davygeeroms.dartsgames.entities.gametypes

import com.davygeeroms.dartsgames.entities.BoardValue
import com.davygeeroms.dartsgames.enums.BoardValues
import com.davygeeroms.dartsgames.enums.GameModes
import com.davygeeroms.dartsgames.interfaces.GameType

class GT501(override val gameMode: GameModes) : GameType {

    override val startScore: Int = 501
    override val targetScore: Int = 0
    override val winModifier: Int = 2
    override val dartsAmount: Int = 3

    override fun hasWon(currentScore: Int, dartThrow: BoardValue): Boolean {

        if(currentScore == targetScore && dartThrow.modifier == winModifier){
            return true
        }
        return false
    }

    override fun calcScore(currentScore: Int, dartThrow: BoardValue) : Int {

        val nextScore = currentScore - (dartThrow.value * dartThrow.modifier)

        if(nextScore < targetScore){
            return currentScore
        }

        if(nextScore == targetScore && dartThrow.modifier != winModifier){
            return currentScore
        }

        return nextScore
    }

    override fun displayedScoreToString(currentScore: Int): String {
        return "Score left: $currentScore"
    }
}