package com.davygeeroms.dartsgames.entities.sportradarAPIResponse

import com.squareup.moshi.Json

class SportEventStatistics(
    @Json(name = "totals")
    val totals: StatTotals
)
