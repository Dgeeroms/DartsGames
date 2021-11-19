package com.davygeeroms.dartsgames.entities.sportradarAPIResponse

import com.squareup.moshi.Json

class DailySummaryAPIResponse(
    @Json(name = "generated_at")
    val timestamp: String,
    @Json(name = "summaries")
    val dailySummaries: List<DailySummary>
)