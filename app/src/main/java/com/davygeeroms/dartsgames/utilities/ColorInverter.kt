package com.davygeeroms.dartsgames.utilities

import android.graphics.Color

class ColorInverter {
    object ColorInverter{
        fun invertColor(myColorString: String): String {
            val myColorStringNoHashTag = myColorString.drop(1)
            val color = myColorStringNoHashTag.toLong(16).toInt()
            val r = color shr 16 and 0xFF
            val g = color shr 8 and 0xFF
            val b = color shr 0 and 0xFF
            val invertedRed = 255 - r
            val invertedGreen = 255 - g
            val invertedBlue = 255 - b
            val invertedColor = Color.rgb(invertedRed, invertedGreen, invertedBlue)
            return "#" + invertedColor.toUInt().toString(16)
        }
    }
}

