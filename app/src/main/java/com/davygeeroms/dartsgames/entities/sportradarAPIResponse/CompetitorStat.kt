package com.davygeeroms.dartsgames.entities.sportradarAPIResponse

import com.squareup.moshi.Json

class CompetitorStat(
    @Json(name = "name")
    val name: String?,
    @Json(name = "abbreviation")
    val abbreviation: String?,
    @Json(name = "qualifier")
    val qualifier: String?,
    @Json(name = "statistics")
    val statisticsDetails: StatisticDetails?
)

