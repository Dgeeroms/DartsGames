package com.davygeeroms.dartsgames.entities

import com.davygeeroms.dartsgames.interfaces.GameType

class Game(val gameType: GameType, var playerScores: List<PlayerScore>) {

    lateinit var currentPlayer: Player
    var currentScore: Int = 501
    var hasWon: Boolean = false
    var dartNumber: Int = 1
    val playerScoreHistory: MutableList<PlayerScoreHistory> = mutableListOf()

    fun startGame(){
        currentPlayer = playerScores.first { p -> p.player.number == 1 }.player
        currentScore = playerScores.first { p -> p.player.number == currentPlayer.number }.score

    }

    fun continueGame(playerNumber: Int){
        currentPlayer = playerScores.first { p -> p.player.number == playerNumber }.player
        currentScore = playerScores.first { p -> p.player.number == playerNumber }.score
    }

    fun throwDart(dart: BoardValue){
        val playerScore = playerScores.first { p -> p.player.number == currentPlayer.number }
        currentScore = gameType.calcScore(playerScore.score, dart)
        playerScores[playerScores.indexOf(playerScore)].score = currentScore
        playerScoreHistory.add(PlayerScoreHistory(playerScore, dart))
        hasWon = gameType.hasWon(playerScore.score, dart)

        if(dartNumber == 3){
            val tmpPlayerScore = playerScores.first { p -> p.player.number == currentPlayer.number.rem(playerScores.size) + 1}
            currentPlayer = tmpPlayerScore.player
            currentScore = tmpPlayerScore.score
        }

        dartNumber = (dartNumber % 3) + 1

    }
}