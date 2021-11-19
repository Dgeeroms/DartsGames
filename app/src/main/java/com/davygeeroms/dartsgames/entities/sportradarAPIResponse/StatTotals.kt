package com.davygeeroms.dartsgames.entities.sportradarAPIResponse

import com.squareup.moshi.Json

class StatTotals(
    @Json(name = "competitors")
    val competitorStats: List<CompetitorStat>
)
