package com.davygeeroms.dartsgames.persistence

import androidx.room.TypeConverter
import com.davygeeroms.dartsgames.entities.BoardValue
import com.davygeeroms.dartsgames.entities.Player
import com.davygeeroms.dartsgames.entities.PlayerScore
import com.davygeeroms.dartsgames.entities.Turn
import com.davygeeroms.dartsgames.enums.BoardValues
import com.davygeeroms.dartsgames.enums.GameModes
import com.davygeeroms.dartsgames.factories.BoardValueFactory
import com.davygeeroms.dartsgames.factories.GameTypeFactory
import com.davygeeroms.dartsgames.interfaces.GameType
import java.time.Instant

class Converters {

    @TypeConverter
    fun fromInstantToString(value: Instant): String{
        return value.toString()
    }

    @TypeConverter
    fun fromStringToInstant(value: String): Instant{
        return Instant.parse(value)
    }

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
    fun fromStringToBoardValueList(value: String): MutableList<BoardValue>{
        val bvl = mutableListOf<BoardValue>()
        if(value != ""){
            val listStr = value.split(",")


            for(str in listStr){
                bvl.add(fromStringToBoardValue(str))
            }
        }
        return bvl

    }

    @TypeConverter
    fun fromBoardValueListToString(value: List<BoardValue>): String{
        var strings = String()

        for(bv in value){
            strings += fromBoardValueToString(bv)
            if(bv != value.last()){
                strings += ","
            }
        }
        return strings
    }

    @TypeConverter
    fun fromTurnListToString(value: List<Turn>) : String{
        var strings = String()

        for(psh in value){
            strings = strings + fromPlayerScoreToString(psh.playerScore) + "|" + fromBoardValueListToString(psh.darts)
            if(psh != value.last()){
                strings += ";"
            }
        }
        return strings
    }

    @TypeConverter
    fun fromTurnToString(value: Turn) : String{
        var strings = String()

        return fromPlayerScoreToString(value.playerScore) + "|" + fromBoardValueListToString(value.darts)

    }

    @TypeConverter
    fun fromStringToTurn(value: String): Turn{
        val splitStr = value.split("|")
        return  Turn(fromStringToPlayerScore(splitStr[0] + "|" + splitStr[1]), fromStringToBoardValueList(splitStr[2]))
    }

    @TypeConverter
    fun fromStringToTurnList(value: String) : List<Turn>{
        if(value.equals("")){
            return mutableListOf()
        }
        val strings = value.split(";")

        val pshList: MutableList<Turn> = mutableListOf()
        for(s in strings){

            pshList.add(fromStringToTurn(s))
        }
        return pshList
    }
}