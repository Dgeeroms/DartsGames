package com.davygeeroms.dartsgames.persistence

import androidx.room.TypeConverter
import com.davygeeroms.dartsgames.entities.BoardValue
import com.davygeeroms.dartsgames.entities.Player
import com.davygeeroms.dartsgames.entities.PlayerScore
import com.davygeeroms.dartsgames.entities.PlayerScoreHistory
import com.davygeeroms.dartsgames.enums.BoardValues
import com.davygeeroms.dartsgames.enums.GameModes
import com.davygeeroms.dartsgames.factories.BoardValueFactory
import com.davygeeroms.dartsgames.factories.GameTypeFactory
import com.davygeeroms.dartsgames.interfaces.GameType

class Converters {

    @TypeConverter
    fun fromGameTypeToString(value: GameType): String{
        return value.gameMode.name
    }

    @TypeConverter
    fun fromStringToGameType(value: String): GameType{
        val gtf = GameTypeFactory()
        return gtf.getGameType(GameModes.valueOf(value))
    }

    @TypeConverter
    fun fromPlayerToString(value: Player): String{
        return value.number.toString() + "," + value.name + "," + value.color
    }

    @TypeConverter
    fun fromStringToPlayer(value: String): Player{
        val strings = value.split(",")
        return Player(strings[0].toInt(), strings[1], strings[2])
    }

    @TypeConverter
    fun fromPlayerScoreToString(value: PlayerScore): String{
        return fromPlayerToString(value.player) + "|" + value.score.toString()
    }

    @TypeConverter
    fun fromStringToPlayerScore(value: String): PlayerScore{
        val playAndScore = value.split("|")
        return PlayerScore(fromStringToPlayer(playAndScore[0]), playAndScore[1].toInt())
    }

    @TypeConverter
    fun fromPlayerScoreListToString(value: List<PlayerScore>) : String{
        var strings = String()
        for(ps in value){
            strings += fromPlayerScoreToString(ps)
            if(ps != value.last()){
                strings += ";"
            }
        }
        return strings
    }

    @TypeConverter
    fun fromStringToPlayerScoreList(value: String): List<PlayerScore>{
        val strings = value.split(";")
        var ps: PlayerScore
        var psList: MutableList<PlayerScore> = mutableListOf()
        for(s in strings){
            psList.add(fromStringToPlayerScore(s))
        }
        return psList
    }

    @TypeConverter
    fun fromBoardValueToString(value: BoardValue): String{
        return value.name
    }

    @TypeConverter
    fun fromStringToBoardValue(value: String): BoardValue{
        val bvf = BoardValueFactory()
        return bvf.getBoardValue(BoardValues.valueOf(value))
    }

    @TypeConverter
    fun fromPlayerScoreHistoryListToString(value: List<PlayerScoreHistory>) : String{
        var strings = String()

        for(psh in value){
            strings = strings + fromPlayerScoreToString(psh.playerScore) + "," + fromBoardValueToString(psh.boardValue)
            if(psh != value.last()){
                strings += ";"
            }
        }
        return strings
    }

    @TypeConverter
    fun fromStringToPlayerScoreHistoryList(value: String) : List<PlayerScoreHistory>{
        if(value.equals("")){
            return mutableListOf()
        }
        val strings = value.split(";")
        var psh: PlayerScoreHistory
        val pshList: MutableList<PlayerScoreHistory> = mutableListOf()
        for(s in strings){
            val pshString = s.split(",")

            pshList.add(PlayerScoreHistory(fromStringToPlayerScore(pshString[0]),fromStringToBoardValue(pshString[1])))
        }
        return pshList
    }

}