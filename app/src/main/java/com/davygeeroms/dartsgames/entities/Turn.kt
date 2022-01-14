package com.davygeeroms.dartsgames.entities

import androidx.room.Ignore
import java.time.Instant

/**
 * A turn
 * @param playerScore combo of a player and his current score and score history (Statistic)
 * @param darts which darts or "BoardValues" have been thrown during this turn. Can be an empty list.
 */
class Turn(var playerScore : PlayerScore, var darts: MutableList<BoardValue> = mutableListOf() ) {

    @Ignore
    var timeStamp: Instant = Instant.now()

    override fun equals(other: Any?): Boolean {

        return hashCode() == (other as Turn).hashCode() && darts.count() == (other as Turn).darts.count()
    }

    override fun hashCode(): Int {
        var result = playerScore.hashCode()
        result = 31 * result + darts.hashCode()
        result = 31 * result + timeStamp.hashCode()
        return result
    }
}