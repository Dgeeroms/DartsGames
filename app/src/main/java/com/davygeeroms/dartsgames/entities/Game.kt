package com.davygeeroms.dartsgames.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.davygeeroms.dartsgames.interfaces.GameType
import com.google.gson.GsonBuilder
import java.sql.Timestamp
import java.time.Instant

@Entity(tableName = "savedGames")
data class Game(
    @ColumnInfo(name = "game_id")
    @PrimaryKey (autoGenerate = true)
    var id: Int = 0,
    var startTime: Instant = Instant.now(),
    val gameType: GameType,
    var playerScores: MutableList<PlayerScore>,
    @ColumnInfo(name = "new_game")
    var newGame: Boolean) {
    lateinit var currentTurn: Turn
    var displayedScoreString: String = ""

    @ColumnInfo(name = "has_won")
    var hasWon: Boolean = false
    var dartNumber: Int = 1
    var playerScoreHistory: MutableList<Turn> = mutableListOf()
   // var stats: Statistics = Statistics()


    fun newGame() {
        currentTurn = Turn(
            PlayerScore(
                playerScores.first { p -> p.player.number == 1 }.player,
                playerScores.first { p -> p.player.number == 1 }.score
            ), mutableListOf()
        )
        dartNumber = updateDartNumber()
        displayedScoreString = gameType.displayedScoreToString(currentTurn.playerScore.score)
    }

    /*
    fun getStats(): List<String> {
        return gameType.statsToString(playerScoreHistory, playerScores)
    }
*/

    /***
     * @param dart
     * Calculates, based on the thrown dart (BoardValue), the new score and if the player has won.
     * If all darts for this turn in the game mode have been thrown, the next player will be selected.
     */
    fun throwDart(dart: BoardValue) {

        if (!hasWon) {

            if(playerScoreHistory.isEmpty()){
                playerScoreHistory.add(currentTurn)
            }

            val tmpScore = gameType.calcScore(currentTurn.playerScore.score, dart)
            //if tmpScore == null it means a faulty throw (kaput) so we need to add previous throws again
            if (tmpScore == null) {
                for (d in currentTurn.darts) {
                    currentTurn.playerScore.score += d.value * d.modifier
                }
            } else {
                currentTurn.darts.add(dart)
                currentTurn.playerScore.score = tmpScore

            }
            updatePlayerScore(currentTurn.playerScore.score)
            playerScoreHistory.removeIf { it.timeStamp == currentTurn.timeStamp }
            playerScoreHistory.add(currentTurn)
            hasWon = gameType.hasWon(currentTurn.playerScore.score, dart)

            //if we are throwing the last dart of the turn -> next player
            if (currentTurn.darts.count() == gameType.dartsAmount && !hasWon) {
                nextPlayer()
            }
            dartNumber = updateDartNumber()
            displayedScoreString = gameType.displayedScoreToString(currentTurn.playerScore.score)

        }
    }

    fun undoThrow() {

        //if we are at dart 1 we need to fetch the last throw from last turn
        //else we just need to go back 1 throw
        if (dartNumber == 1) {
            currentTurn = playerScoreHistory[playerScoreHistory.count() - 1]
            playerScoreHistory.dropLast(1)
        } else {
            currentTurn.darts.dropLast(1)
        }

        dartNumber = updateDartNumber()
        displayedScoreString = gameType.displayedScoreToString(currentTurn.playerScore.score)

    }


    private fun updateDartNumber(): Int {
        if (currentTurn.darts.count() < gameType.dartsAmount) {
            return currentTurn.darts.count() + 1
        }
        return currentTurn.darts.count()
    }

    private fun updatePlayerScore(score: Int){

        val currentPlayerNb = currentTurn.playerScore.player.number
        playerScores.first { p -> p.player.number == currentPlayerNb}.score = score
    }

    private fun nextPlayer() {

        val currentPlayerNb = currentTurn.playerScore.player.number

        currentTurn = Turn(playerScores.first { p -> p.player.number == ((currentPlayerNb % playerScores.count()) + 1) }, mutableListOf())

    }
}

