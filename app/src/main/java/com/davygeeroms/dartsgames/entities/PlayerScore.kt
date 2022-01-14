package com.davygeeroms.dartsgames.entities

/**
 * Class PlayerScore is a combo of the current score and statistics for a player
 * @param player: The player for whom this PlayerScore is
 * @param score: The current score
 * @param stat: The statistics related to the player's score
 */
class PlayerScore(var player: Player, var score: Int, var stat: Statistic) {

    override fun equals(other: Any?): Boolean {

        return hashCode() == (other as PlayerScore).hashCode() && stat == other.stat
    }

    override fun hashCode(): Int {
        var result = player.hashCode()
        result = 31 * result + score.hashCode()
        result = 31 * result + stat.hashCode()

        return result
    }

}
