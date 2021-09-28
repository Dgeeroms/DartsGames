package com.davygeeroms.dartsgames.entities

import java.sql.Timestamp
import java.time.Instant

class PlayerScoreHistory(val playerScore: PlayerScore, val boardValue: BoardValue){
    val timestamp: Timestamp = Timestamp(System.currentTimeMillis())

    override fun equals(other: Any?): Boolean = (this === other) || (
                                    (other is PlayerScoreHistory) &&
                                    this.timestamp.equals(other.timestamp) &&
                                    this.playerScore.score.equals(other.playerScore.score) &&
                                    this.playerScore.player.number.equals(other.playerScore.player.number) &&
                                    this.boardValue.description.equals(other.boardValue.description)
                                    )

}