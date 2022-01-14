package com.davygeeroms.dartsgames.entities.gametypes

import com.davygeeroms.dartsgames.entities.BoardValue
import com.davygeeroms.dartsgames.entities.Turn
import com.davygeeroms.dartsgames.enums.BoardValues
import com.davygeeroms.dartsgames.factories.BoardValueFactory
import com.davygeeroms.dartsgames.interfaces.GameType

/**
 * Class for GameType. Throw darts in the single fields of 1 to 20 sequentially.
 * @property description: String describing the game type
 * @property startScore: Score which the players start with
 * @property targetScore: Score where the game ends
 * @property winModifier: Which modifier does the "winning" dart need to have in order to win?
 * @property dartsAmount: Amount of darts that each player has per Turn
 * @property checkOutTable: Boolean that determines if the checkout table should be shown in this game mode
 */
class GTClockSingle() : GameType {

    override val description = "AROUNDTHECLOCK"
    override val startScore: Int = 20
    override val targetScore: Int = 0
    override val winModifier: Int = 1
    override val dartsAmount: Int = 3
    override val checkOutTable: Boolean = false

    /**
     * Determines if the game has ended because of this dart
     * @param currentScore: Player's current score
     * @param dartThrow: The dart that was thrown
     * @return Boolean stating if the game was won or not
     */
    override fun hasWon(currentScore: Int, dartThrow: BoardValue): Boolean {

        if(currentScore == targetScore && dartThrow.modifier == winModifier){
            return true
        }
        return false
    }

    /**
     * Determines if the dart was an actual hit relevant to this game type
     * @param currentScore: Player's current score
     * @param dartThrow: The dart that was thrown
     * @return Boolean stating if the current throw was a hit or not
     */
    override fun wasHit(currentScore: Int, dartThrow: BoardValue): Boolean {
        return currentScore - dartThrow.value == 0
    }

    /**
     * Calculates score to be after dart throw
     * @param currentScore: Player's current score
     * @param dartThrow: The dart that was thrown
     * @return nullable Int showing the new score - null if the throw was invalid (below 0, wrong modifier, etc)
     */
    override fun calcScore(currentScore: Int, dartThrow: BoardValue) : Int {
        if(currentScore - dartThrow.value == 0){
            return currentScore - 1
        }
        return currentScore
    }

    /**
     * Writes out the String to represent a score
     * @param currentScore: Player's current score
     * @return String which include current score
     */
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