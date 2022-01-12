package com.davygeeroms.dartsgames.entities.sportradarAPIResponse

import com.squareup.moshi.Json

class SportEventContext(
    @Json(name = "competition")
    val competition: Competition?,
    @Json(name = "season")
    val season: Season?
)