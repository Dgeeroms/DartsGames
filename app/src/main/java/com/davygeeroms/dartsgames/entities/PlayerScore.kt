package com.davygeeroms.dartsgames.entities

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
