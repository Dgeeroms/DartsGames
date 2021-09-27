package com.davygeeroms.dartsgames.entities.gametypes

import com.davygeeroms.dartsgames.entities.BoardValue
import com.davygeeroms.dartsgames.entities.PlayerScore
import com.davygeeroms.dartsgames.entities.PlayerScoreHistory
import com.davygeeroms.dartsgames.enums.BoardValues
import com.davygeeroms.dartsgames.enums.GameModes
import com.davygeeroms.dartsgames.factories.BoardValueFactory
import com.davygeeroms.dartsgames.interfaces.GameType

class GTClockSingle(override val gameMode: GameModes) : GameType {

    override val startScore: Int = 20
    override val targetScore: Int = 0
    override val winModifier: Int = 1
    override val dartsAmount: Int = 1

    override fun hasWon(currentScore: Int, dartThrow: BoardValue): Boolean {

        if(currentScore == targetScore && dartThrow.modifier == winModifier){
            return true
        }
        return false
    }

    override fun calcScore(currentScore: Int, dartThrow: BoardValue) : Int {
        if(currentScore - dartThrow.value == 0){
            return currentScore - 1
        }
        return currentScore
    }

    override fun displayedScoreToString(currentScore: Int): String {

        val bvf = BoardValueFactory()
        val scoreString: String

        if(currentScore == 0) {
            scoreString = "End of game"
        } else if(currentScore < 10){
            scoreString = bvf.getBoardValue(BoardValues.valueOf("S0$currentScore")).description
        } else {
            scoreString = bvf.getBoardValue(BoardValues.valueOf("S$currentScore")).description
        }

        return "Target: $scoreString"
    }

    override fun statsToString(history: List<PlayerScoreHistory>, players: List<PlayerScore>): List<String> {
        val result: MutableList<String> = mutableListOf()

        for (p in players){
            var playerStat: String
            playerStat = "Player: ${p.player.number} - ${p.player.name}\n"
            playerStat += "Under construction\n"

            result.add(playerStat)
        }
        return result
    }
}