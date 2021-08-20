package com.davygeeroms.dartsgames.entities

import com.squareup.moshi.Json

class WeatherDetails (

    @Json(name = "weather")
    val weather: List<Weather>,
    @Json(name = "main")
    val temperature: Temperature

)