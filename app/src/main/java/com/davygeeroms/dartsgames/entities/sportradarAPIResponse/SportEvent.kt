package com.davygeeroms.dartsgames.entities.sportradarAPIResponse

import com.squareup.moshi.Json

class SportEvent(

    @Json(name = "id")
    val id: String?,
    @Json(name = "start_time")
    val startTime: String?,
    @Json(name = "sport_event_context")
    val sportEventContext: SportEventContext?,
    @Json(name = "competitors")
    val competitors : List<Competitor>?
)