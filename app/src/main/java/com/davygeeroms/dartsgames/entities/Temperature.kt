package com.davygeeroms.dartsgames.entities

import com.squareup.moshi.Json

class Temperature(

    @Json(name = "temp")
    var temperature: Float

)
