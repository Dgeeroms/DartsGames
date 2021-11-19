package com.davygeeroms.dartsgames.entities.sportradarAPIResponse

import com.squareup.moshi.Json

class SportEventStatus (
    @Json(name = "match_status")
    val matchStatus: String,
    @Json(name = "home_score")
    val homeScore: Int?,
    @Json(name = "away_score")
    val awayScore: Int?
)
