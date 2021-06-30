package com.davygeeroms.dartsgames.entities

import com.davygeeroms.dartsgames.interfaces.GameType

class Game(val gameType: GameType, var playerScores: List<PlayerScore>) {

    lateinit var currentPlayer: Player
    var currentScore: Int = 501
    var hasWon: Boolean = false
    var dartNumber: Int = 1

    fun startGame(){
        currentPlayer = playerScores.first { p -> p.player.number == 1 }.player
        currentScore = playerScores.first { p -> p.player.number == currentPlayer.number }.score

    }

    fun continueGame(playerNumber: Int){
        currentPlayer = playerScores.first { p -> p.player.number == playerNumber }.player
        currentScore = playerScores.first { p -> p.player.number == playerNumber }.score
    }

    fun throwDart(dart: BoardValue){
        currentScore = gameType.calcScore(playerScores.first { p -> p.player.number == currentPlayer.number }.score, dart)
        hasWon = gameType.hasWon(playerScores.first { p -> p.player.number == currentPlayer.number }.score, dart)

        // next player
        currentPlayer = playerScores.first { p -> p.player.number == currentPlayer.number.rem(playerScores.size) + 1}.player
        currentScore = playerScores.first { p -> p.player.number == currentPlayer.number }.score

        dartNumber = (dartNumber % 3) + 1

    }
}