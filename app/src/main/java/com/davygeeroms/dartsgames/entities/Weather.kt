package com.davygeeroms.dartsgames.entities

import com.squareup.moshi.Json

class Weather (
    @Json(name = "description")
    val weather: String

)