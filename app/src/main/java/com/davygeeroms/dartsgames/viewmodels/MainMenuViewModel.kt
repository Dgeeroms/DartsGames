package com.davygeeroms.dartsgames.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.davygeeroms.dartsgames.entities.WeatherDetails
import com.davygeeroms.dartsgames.network.WeatherApi
import kotlinx.coroutines.*

class MainMenuViewModel(application: Application) : AndroidViewModel(application) {


    //Coroutine
    private var vmJob = Job()
    private var uiScope = CoroutineScope(Dispatchers.Main + vmJob)

    private var _weather = MutableLiveData<WeatherDetails>()
    val weather : LiveData<WeatherDetails>
        get() = _weather

    init {
        getWeather()
    }


    private fun getWeather(){
        uiScope.launch {
            getWeatherFromAPI()
        }
    }

    private suspend fun getWeatherFromAPI(): Any? {
        return withContext(Dispatchers.IO){
            val weatherDef = WeatherApi.retrofitService.getWeatherAsync()

            try{
                val weather = weatherDef.await()
                weather.temperature.temperature = weather.temperature.temperature - 273.15F
                _weather.postValue(weather)
            }
            catch (t: Throwable){
                t.message?.let { Log.i("Response NOK test", it) }
            }
        }
    }
}