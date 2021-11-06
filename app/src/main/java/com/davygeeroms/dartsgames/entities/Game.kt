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


    fun newGame() {
        currentTurn = Turn(
            PlayerScore(
                playerScores.first { p -> p.player.number == 1 }.player,
                playerScores.first { p -> p.player.number == 1 }.score,
                playerScores.first { p -> p.player.number == 1 }.stat
            ), mutableListOf()
        )
        dartNumber = updateDartNumber()
        displayedScoreString = gameType.displayedScoreToString(currentTurn.playerScore.score)
    }

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
                    currentTurn.playerScore.stat.miss()
                }
            } else {
                currentTurn.darts.add(dart)
                currentTurn.playerScore.score = tmpScore
                if(gameType.wasHit(currentTurn.playerScore.score, dart)){
                    currentTurn.playerScore.stat.hit()
                } else {
                    currentTurn.playerScore.stat.miss()
                }

            }
            updatePlayerScore()
            playerScoreHistory.removeIf { it.timeStamp == currentTurn.timeStamp }
            playerScoreHistory.add(currentTurn)
            hasWon = gameType.hasWon(currentTurn.playerScore.score, dart)
            if(hasWon){
                currentTurn.playerScore.stat.updateCheckOut(currentTurn.darts)
            }

            //if we are throwing the last dart of the turn -> next player
            if (currentTurn.darts.count() == gameType.dartsAmount && !hasWon) {
                nextPlayer()
            }
            dartNumber = updateDartNumber()
            displayedScoreString = gameType.displayedScoreToString(currentTurn.playerScore.score)

        }
    }

    fun undoThrow(lastThrow : Turn) {

        //if we are at dart 1 we need to fetch the last throw from last turn
        //else we just need to go back 1 throw
        lastThrow.darts.removeAt(lastThrow.darts.lastIndex)
        currentTurn = lastThrow
        playerScoreHistory.removeAt(playerScoreHistory.lastIndex)
        playerScoreHistory.add(currentTurn)


        dartNumber = updateDartNumber()
        updatePlayerScore()
        displayedScoreString = gameType.displayedScoreToString(currentTurn.playerScore.score)

    }


    private fun updateDartNumber(): Int {
        if (currentTurn.darts.count() < gameType.dartsAmount) {
            return currentTurn.darts.count() + 1
        }
        return currentTurn.darts.count()
    }

    private fun updatePlayerScore(){

        val currentPlayerNb = currentTurn.playerScore.player.number

        val index = playerScores.indexOf(playerScores.first { p -> p.player.number == currentPlayerNb})
        playerScores[index] = currentTurn.playerScore

    }

    private fun nextPlayer() {

        val currentPlayerNb = currentTurn.playerScore.player.number

        //update stats of current player before going to next player
        currentTurn.playerScore.stat.updateStats(currentTurn.darts)

        currentTurn = Turn(playerScores.first { p -> p.player.number == ((currentPlayerNb % playerScores.count()) + 1) }, mutableListOf())

    }
}

