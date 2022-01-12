package com.davygeeroms.dartsgames.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.davygeeroms.dartsgames.entities.sportradarAPIResponse.DailySummaryAPIResponse
import com.davygeeroms.dartsgames.network.SportAPI
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * MainMenuViewModel
 */
class MainMenuViewModel(application: Application) : AndroidViewModel(application) {


    //Coroutine
    private var vmJob = Job()
    private var uiScope = CoroutineScope(Dispatchers.Main + vmJob)

    private var _dailySummary = MutableLiveData<DailySummaryAPIResponse>()
    val dailySummary : LiveData<DailySummaryAPIResponse>
        get() = _dailySummary

    init {
        val today = LocalDateTime.now()
        getDailySummaries(today.year.toString(), today.monthValue, today.dayOfMonth)
        //getDailySummaries("2021","11","18")
    }

    /**
     * Adds a leading 0 to months/days below 10
     * @param value day or month
     * @return the string value with added 0
     */
    private fun dateAddLeadingZero(value: Int) : String{
        if(value < 10){
            return "0$value"
        }
        return value.toString()
    }

    /**
     * Launch side thread to call SportRadar API updates LiveData
     * @param year month day arguments
     */
    fun getDailySummaries(year: String, month: Int, day: Int){
        uiScope.launch {
            _dailySummary.value = getDailySummariesAPI(year, dateAddLeadingZero(month), dateAddLeadingZero(day))
        }
    }

    /**
     * Launches IO thread to call SportRadar API updates LiveData
     * @param year of which summary is needed
     * @param month of which summary is needed
     * @param day of which summary is needed
     *
     * @return DailySummaryAPIResponse object from JSON
     *
     */
    private suspend fun getDailySummariesAPI(year: String, month: String, day: String): DailySummaryAPIResponse? {
        return withContext(Dispatchers.IO){

             SportAPI.getDailySummaries(year, month, day)

        }
    }
}