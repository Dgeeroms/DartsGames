package com.davygeeroms.dartsgames.entities

import androidx.room.Ignore
import java.time.Instant

class Turn(var playerScore : PlayerScore, var darts: MutableList<BoardValue> = mutableListOf<BoardValue>() ) {

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