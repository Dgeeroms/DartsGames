package com.davygeeroms.dartsgames.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.davygeeroms.dartsgames.interfaces.GameType
import com.google.gson.GsonBuilder
import java.sql.Timestamp
import java.time.Instant

/**
 * A darts game.
 *
 * @param id: Unique number referring to this game.
 * @param startTime: The Instant when the game was started.
 * @param gameType: Which game type is being played
 * @param playerScores: List of score and statistics of a player, so for each player a playerScore obj
 * @param newGame: Is this a "New Game"? -> Used to identify the game that was just started between NewGame and PlayGame fragments.
 * @property currentTurn: The current ongoing turn
 * @property displayedScoreString: The String version of the score for the current playing player
 * @property hasWon: Has the game been won by anyone? Eg.: has the game ended?
 * @property dartNumber: The number of the dart that is currently being thrown. Usually 1-3.
 * @property playerScoreHistory: Stores the playerScore in list after throwing to have a history.
 */
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

    /**
     * Initialization to be ready for a new game
     */
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

    /**
     * Undoes last throw
     * @param lastThrow: Previous Turn object that will take the place of the current Turn
     */
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

    /**
     * Calculates what dart number should be shown based on already thrown darts
     * @return current dart number
     */
    private fun updateDartNumber(): Int {
        if (currentTurn.darts.count() < gameType.dartsAmount) {
            return currentTurn.darts.count() + 1
        }
        return currentTurn.darts.count()
    }

    /**
     * Updates the playerScore of the player who just threw which the score recorded in currentTurn
     */
    private fun updatePlayerScore(){

        val currentPlayerNb = currentTurn.playerScore.player.number

        val index = playerScores.indexOf(playerScores.first { p -> p.player.number == currentPlayerNb})
        playerScores[index] = currentTurn.playerScore

    }

    /**
     * Cycles through the PlayerScore list to appoint a new player and create a new Turn as current turn
     */
    private fun nextPlayer() {

        val currentPlayerNb = currentTurn.playerScore.player.number

        //update stats of current player before going to next player
        currentTurn.playerScore.stat.updateStats(currentTurn.darts)

        currentTurn = Turn(playerScores.first { p -> p.player.number == ((currentPlayerNb % playerScores.count()) + 1) }, mutableListOf())

    }
}

