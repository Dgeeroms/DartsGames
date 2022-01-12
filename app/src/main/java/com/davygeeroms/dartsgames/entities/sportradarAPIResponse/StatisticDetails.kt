package com.davygeeroms.dartsgames.entities.sportradarAPIResponse

import com.squareup.moshi.Json

class StatisticDetails(
    @Json(name = "average_3_darts")
    val avgThreeDarts: Float?,
    @Json(name = "checkout_percentage")
    val checkoutPercentage: Float?,
    @Json(name = "checkouts")
    val checkouts: Int?,
    @Json(name = "checkouts_100s_plus")
    val checkout100plus: Int?,
    @Json(name = "highest_checkout")
    val highestCheckout : Int?,
    @Json(name = "scores_100s_plus")
    val times100plus: Int?,
    @Json(name = "scores_140s_plus")
    val times140plus: Int?,
    @Json(name = "scores_180s")
    val oneEighties: Int?
)
