package com.davygeeroms.dartsgames.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.davygeeroms.dartsgames.interfaces.GameType
import com.google.gson.GsonBuilder

@Entity(tableName = "savedGames")
data class Game(
    @ColumnInfo(name = "game_id")
    @PrimaryKey (autoGenerate = true)
    var id: Int = 0,
    val gameType: GameType,
    var playerScores: MutableList<PlayerScore>,
    @ColumnInfo(name = "new_game")
    var newGame: Boolean) {

    lateinit var currentPlayer: Player
    var currentScore: Int = 501
    var displayedString: String = ""
    var hasWon: Boolean = false
    var dartNumber: Int = 1
    var playerScoreHistory: MutableList<PlayerScoreHistory> = mutableListOf()


    fun startGame() {
        currentPlayer = playerScores.first { p -> p.player.number == 1 }.player
        currentScore = playerScores.first { p -> p.player.number == currentPlayer.number }.score
        displayedString = gameType.displayedScoreToString(currentScore)
    }

    fun getStats(): List<String> {
        return gameType.statsToString(playerScoreHistory, playerScores)
    }

    fun getLastPlayerScoreHistory(): PlayerScoreHistory{
        return playerScoreHistory.last()
    }

    fun undoThrow(psh: PlayerScoreHistory){

        dartNumber = updateDartNumber(false)

        currentPlayer = psh.playerScore.player
        currentScore =  psh.playerScore.score


        val playerScore = playerScores.first { p -> p.player.number == psh.playerScore.player.number }
        currentScore += psh.boardValue.value * psh.boardValue.modifier
        displayedString = gameType.displayedScoreToString(currentScore)
        playerScores[playerScores.indexOf(playerScore)].score = currentScore
        playerScoreHistory.remove(psh)





    }


    /***
     * @param dart
     * Calculates, based on the thrown dart (BoardValue), the new score and if the player has won.
     * If all darts for this turn in the game mode have been thrown, the next player will be selected.
     */
    fun throwDart(dart: BoardValue) {
        if (!hasWon) {
            val playerScore = playerScores.first { p -> p.player.number == currentPlayer.number }
            currentScore = gameType.calcScore(playerScore.score, dart)
            displayedString = gameType.displayedScoreToString(currentScore)
            playerScores[playerScores.indexOf(playerScore)].score = currentScore
            playerScoreHistory.add(
                PlayerScoreHistory(
                    PlayerScore(currentPlayer, currentScore),
                    dart
                )
            )
            hasWon = gameType.hasWon(playerScore.score, dart)

            if (dartNumber == gameType.dartsAmount && !hasWon) {
                val tmpPlayerScore = playerScores.first { p ->
                    p.player.number == currentPlayer.number.rem(playerScores.size) + 1
                }
                currentPlayer = tmpPlayerScore.player
                currentScore = tmpPlayerScore.score
                displayedString = gameType.displayedScoreToString(currentScore)
            }
            if (!hasWon) {
                dartNumber = updateDartNumber(true)
            }
        }
    }

    private fun updateDartNumber(upOrDown: Boolean): Int{

        //true = increase
        //false = decrease
        if(upOrDown){
            return (dartNumber % gameType.dartsAmount) + 1
        } else {

            if (dartNumber == 1){
                return gameType.dartsAmount
            }
            return dartNumber - 1

        }
        return  ((dartNumber - 1) % gameType.dartsAmount)
    }

}