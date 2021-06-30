package com.davygeeroms.dartsgames.entities.gametypes

import com.davygeeroms.dartsgames.entities.BoardValue
import com.davygeeroms.dartsgames.enums.BoardValues
import com.davygeeroms.dartsgames.enums.GameModes
import com.davygeeroms.dartsgames.interfaces.GameType

class GTClockSingle(override val gameMode: GameModes) : GameType {

    override val startScore: Int = 20
    override val targetScore: Int = 0
    override val winModifier: Int = 1

    override fun hasWon(currentScore: Int, dartThrow: BoardValue): Boolean {

        if(currentScore == targetScore && dartThrow.modifier == winModifier){
            return true
        }
        return false
    }

    override fun calcScore(currentScore: Int, dartThrow: BoardValue) : Int {
        if(currentScore - dartThrow.value == 1){
            return currentScore - 1
        }
        return currentScore
    }

}