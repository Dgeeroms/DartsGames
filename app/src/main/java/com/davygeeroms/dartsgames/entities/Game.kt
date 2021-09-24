package com.davygeeroms.dartsgames.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.davygeeroms.dartsgames.interfaces.GameType

@Entity(tableName = "savedGames")
data class Game(
    @ColumnInfo(name = "game_id")
    @PrimaryKey (autoGenerate = true)
    var id: Int = 0,
    val gameType: GameType,
    var playerScores: List<PlayerScore>,
    @ColumnInfo(name = "new_game")
    var newGame: Boolean)
{

        lateinit var currentPlayer: Player
        var currentScore: Int = 501
        var displayedString: String = ""
        var hasWon: Boolean = false
        var dartNumber: Int = 1
        var playerScoreHistory: MutableList<PlayerScoreHistory> = mutableListOf()

        fun startGame(){
            currentPlayer = playerScores.first { p -> p.player.number == 1 }.player
            currentScore = playerScores.first { p -> p.player.number == currentPlayer.number }.score
            displayedString = gameType.displayedScoreToString(currentScore)
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
                dartNumber = (dartNumber % gameType.dartsAmount) + 1
            }
        }
    }
}