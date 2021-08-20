package com.davygeeroms.dartsgames.factories

import com.davygeeroms.dartsgames.entities.BoardValue
import com.davygeeroms.dartsgames.enums.BoardValues

class BoardValueFactory {

    fun getBoardValue(bv:BoardValues): BoardValue{

        val modifierStr = bv.name.substring(0,1)
        var modifierInt = 1
        when(modifierStr){
            "S" -> modifierInt = 1
            "D" -> modifierInt = 2
            "T" -> modifierInt = 3
        }

        return BoardValue(
            bv.name,
            bv.name.substring(1).toInt(),
            modifierInt,
            bv.valuesStr
        )
    }
}