package com.davygeeroms.dartsgames.network

import android.util.Log
import com.davygeeroms.dartsgames.entities.sportradarAPIResponse.DailySummaryAPIResponse
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


private var baseUrl = "http://api.sportradar.us/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private var retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(baseUrl)
    .build()

/**
 * API service calling Sport Radar API used in the main menu
 */
interface SportRadarAPIService {
    /**
     * Calls API and gets a daily summary of TV darts matches
     * @param year: The year in which the match occurred / occurs
     * @param month: The month in which the match occurred / occurs
     * @param day: The day when the match occurred / occurs
     * @param key: The key required by Sport Radar API
     * @return A DailySummaryAPIResponse object containing the data for the requested date
     */
    @GET("darts/trial/v2/en/schedules/{year}-{month}-{day}/summaries.json")
    fun getDailyDartsSummary(
        @Path("year")
        year: String,
        @Path("month")
        month: String,
        @Path("day")
        day: String,
        @Query("api_key")
        key: String

    ) : Deferred<DailySummaryAPIResponse>
}

object SportAPI{
    val retrofitService: SportRadarAPIService by lazy {
        retrofit.create(SportRadarAPIService::class.java)
    }

    suspend fun getDailySummaries(year: String, month: String, day: String): DailySummaryAPIResponse?{

        var dailyS: DailySummaryAPIResponse? = null

        val dailySDef = SportAPI.retrofitService.getDailyDartsSummary(year, month, day, "sz5prdaqzvpj7328ykwbamhg")

        try {
            dailyS = dailySDef.await()
        } catch (t: Throwable) {
            t.message?.let {
                Log.i("Response NOK test", it)
            }

        }
        return dailyS

    }
}