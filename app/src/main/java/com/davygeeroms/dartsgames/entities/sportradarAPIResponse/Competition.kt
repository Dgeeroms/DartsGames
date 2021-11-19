package com.davygeeroms.dartsgames.entities.sportradarAPIResponse

import com.squareup.moshi.Json

class Competition (
    @Json(name = "name")
    val name : String
)