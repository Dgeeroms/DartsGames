package com.davygeeroms.dartsgames.entities.sportradarAPIResponse

import com.davygeeroms.dartsgames.entities.Turn
import com.squareup.moshi.Json

class DailySummary(
    @Json(name = "sport_event")
    val sportEvent: SportEvent,
    @Json(name = "sport_event_status")
    val sportEventStatus : SportEventStatus,
    @Json(name = "statistics")
    val statistics: SportEventStatistics?
){

    override fun equals(other: Any?): Boolean {

        return hashCode() == (other as DailySummary).hashCode() && sportEvent.id == (other as DailySummary).sportEvent.id
    }

    override fun hashCode(): Int {
        var result = sportEvent.hashCode()
        result = 31 * result + sportEventStatus.hashCode()
        result = 31 * result + statistics.hashCode()
        return result
    }

}