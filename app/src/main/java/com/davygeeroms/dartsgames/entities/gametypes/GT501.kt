package com.davygeeroms.dartsgames.entities.gametypes

import com.davygeeroms.dartsgames.entities.BoardValue
import com.davygeeroms.dartsgames.entities.Turn
import com.davygeeroms.dartsgames.interfaces.GameType
/**
 * Class for GameType. Get the score ASAP from 501 to 0, BUT the dart leading to 0 must strike a Double field.
 * @property description: String describing the game type
 * @property startScore: Score which the players start with
 * @property targetScore: Score where the game ends
 * @property winModifier: Which modifier does the "winning" dart need to have in order to win?
 * @property dartsAmount: Amount of darts that each player has per Turn
 * @property checkOutTable: Boolean that determines if the checkout table should be shown in this game mode
 */
class GT501() : GameType {

    override val description = "FIVEHUNDREDANDONE"
    override val startScore: Int = 501
    override val targetScore: Int = 0
    override val winModifier: Int = 2
    override val dartsAmount: Int = 3
    override val checkOutTable: Boolean = true

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
        return dartThrow.id != 0
    }

    /**
     * Calculates score to be after dart throw
     * @param currentScore: Player's current score
     * @param dartThrow: The dart that was thrown
     * @return nullable Int showing the new score - null if the throw was invalid (below 0, wrong modifier, etc)
     */
    override fun calcScore(currentScore: Int, dartThrow: BoardValue) : Int? {

        val nextScore = currentScore - (dartThrow.value * dartThrow.modifier)

        if(nextScore < targetScore || (nextScore == targetScore && dartThrow.modifier != winModifier)){
            return null
        }

        return nextScore
    }

    /**
     * Writes out the String to represent a score
     * @param currentScore: Player's current score
     * @return String which include current score
     */
    override fun displayedScoreToString(currentScore: Int): String {
        return "Score left: $currentScore"
    }

}