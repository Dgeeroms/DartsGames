package com.davygeeroms.dartsgames.network

import android.util.Log
import com.davygeeroms.dartsgames.entities.WeatherDetails
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

private var baseUrl = "https://api.openweathermap.org/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private var retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(baseUrl)
    .build()


interface WeatherAPIService
 {

        @GET("data/2.5/weather?q=geraardsbergen,be&appid=df89de08dcc13ac1070ed20d46939872")
        fun getWeatherAsync():
                Deferred<WeatherDetails>

    }



    object WeatherApi{
        val retrofitService : WeatherAPIService by lazy{
            retrofit.create(WeatherAPIService::class.java)
        }

        suspend fun getWeather(): WeatherDetails?
        {
            var weather: WeatherDetails? = null
            val weatherDef = retrofitService.getWeatherAsync()
            try{
                weather = weatherDef.await()

            }catch (t: Throwable){
                t.message?.let { Log.i("Response NOK test", it) }
            }

            return weather
        }
    }