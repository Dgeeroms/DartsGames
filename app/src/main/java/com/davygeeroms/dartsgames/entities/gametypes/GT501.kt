package com.davygeeroms.dartsgames.entities.gametypes

import com.davygeeroms.dartsgames.entities.BoardValue
import com.davygeeroms.dartsgames.entities.Player
import com.davygeeroms.dartsgames.entities.PlayerScore
import com.davygeeroms.dartsgames.entities.PlayerScoreHistory
import com.davygeeroms.dartsgames.enums.BoardValues
import com.davygeeroms.dartsgames.enums.GameModes
import com.davygeeroms.dartsgames.interfaces.GameType

class GT501(override val gameMode: GameModes) : GameType {

    override val startScore: Int = 501
    override val targetScore: Int = 0
    override val winModifier: Int = 2
    override val dartsAmount: Int = 3

    override fun hasWon(currentScore: Int, dartThrow: BoardValue): Boolean {

        if(currentScore == targetScore && dartThrow.modifier == winModifier){
            return true
        }
        return false
    }

    override fun calcScore(currentScore: Int, dartThrow: BoardValue) : Int {

        val nextScore = currentScore - (dartThrow.value * dartThrow.modifier)

        if(nextScore < targetScore){
            return currentScore
        }

        if(nextScore == targetScore && dartThrow.modifier != winModifier){
            return currentScore
        }

        return nextScore
    }

    override fun displayedScoreToString(currentScore: Int): String {
        return "Score left: $currentScore"
    }

    override fun statsToString(history: List<PlayerScoreHistory>, players: List<PlayerScore>): List<String> {
        val result: MutableList<String> = mutableListOf()
        // sort players by end score -> winner on top
        players.sortedBy { it.score }
        // each player will have a textview in the linear list
        for (p in players){
            var playerStat: String
            playerStat = "Player ${p.player.number}: ${p.player.name}\n"
            playerStat += "End score: ${p.score}\n"
            playerStat += "Highest throw: " + highestThrow(history, p.player) + "\n"
            playerStat += "Average: " + average(history, p.player).toString() + "\n"

            result.add(playerStat)
        }
        return result
    }

    private fun highestThrow(history: List<PlayerScoreHistory>, player: Player): Int{
        var score = 0
        var endScore = 0
        for(i in history.indices){
            //score for these 3 darts
            if(history[i].playerScore.player.number == player.number){
                score += history[i].boardValue.value * history[i].boardValue.modifier
                if(endScore < score){
                    endScore = score
                }
            } else
            //another player threw -> new round
            {
                score = 0
            }
        }
        return endScore
    }

    private fun average(history: List<PlayerScoreHistory>, player: Player): Float{

        var totScore = 0
        val filteredList = history.filter { h -> h.playerScore.player.number == player.number }

        for(f in filteredList){
            totScore += f.boardValue.value * f.boardValue.modifier
        }

        return totScore.toFloat() / filteredList.count()

    }
}