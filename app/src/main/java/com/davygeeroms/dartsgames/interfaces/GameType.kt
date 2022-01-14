package com.davygeeroms.dartsgames.interfaces

import com.davygeeroms.dartsgames.entities.BoardValue
import com.davygeeroms.dartsgames.entities.PlayerScore
import com.davygeeroms.dartsgames.enums.GameModes

/**
 * Interface for GameTypes.
 * See classes implementing this for detailed info
 */
interface GameType {

    val description: String
    val targetScore: Int
    val startScore: Int
    val winModifier: Int
    val dartsAmount: Int
    val checkOutTable: Boolean

    fun hasWon(currentScore: Int, dartThrow : BoardValue): Boolean
    fun wasHit(currentScore: Int, dartThrow: BoardValue): Boolean
    fun calcScore(currentScore: Int, dartThrow: BoardValue): Int?
    fun displayedScoreToString(currentScore: Int): String

}