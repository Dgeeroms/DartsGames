package com.davygeeroms.dartsgames.factories

import com.davygeeroms.dartsgames.entities.BoardValue
import com.davygeeroms.dartsgames.enums.BoardValues

/**
 * Board Value factory. Enum goes in, BoardValue comes out.
 */
class BoardValueFactory {

    /**
     * Return BoardValue object corresponding to Enum
     * @param bv: Enum board value
     * @return Corresponding BoardValue obj
     */
    fun getBoardValue(bv:BoardValues): BoardValue{

        val modifierStr = bv.name.substring(0,1)
        var modifierInt = 1
        when(modifierStr){
            "S" -> modifierInt = 1
            "D" -> modifierInt = 2
            "T" -> modifierInt = 3
        }

        return BoardValue(
            bv.id,
            bv.name,
            bv.name.substring(1).toInt(),
            modifierInt,
            bv.valuesStr
        )
    }
}