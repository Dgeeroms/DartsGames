package com.davygeeroms.dartsgames.factories

import com.davygeeroms.dartsgames.entities.gametypes.GT301
import com.davygeeroms.dartsgames.entities.gametypes.GT501
import com.davygeeroms.dartsgames.entities.gametypes.GTClockSingle
import com.davygeeroms.dartsgames.entities.gametypes.GTRandomBV
import com.davygeeroms.dartsgames.enums.GameModes
import com.davygeeroms.dartsgames.interfaces.GameType

class GameTypeFactory() {

    fun getGameType(gm: GameModes) : GameType {

        when (gm.name){
            "THREEHUNDREDANDONE" -> return GT301()
            "FIVEHUNDREDANDONE" -> return GT501()
            "AROUNDTHECLOCK" -> return GTClockSingle()
            "RANDOMBOARDVALUES" -> return GTRandomBV()
        }
        //default
        return GT501()
    }

}