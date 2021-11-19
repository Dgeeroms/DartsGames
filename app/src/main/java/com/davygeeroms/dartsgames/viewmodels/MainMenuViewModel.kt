package com.davygeeroms.dartsgames.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.davygeeroms.dartsgames.entities.sportradarAPIResponse.DailySummaryAPIResponse
import com.davygeeroms.dartsgames.network.SportAPI
import kotlinx.coroutines.*
import java.time.LocalDateTime

class MainMenuViewModel(application: Application) : AndroidViewModel(application) {


    //Coroutine
    private var vmJob = Job()
    private var uiScope = CoroutineScope(Dispatchers.Main + vmJob)

    private var _dailySummary = MutableLiveData<DailySummaryAPIResponse>()
    val dailySummary : LiveData<DailySummaryAPIResponse>
        get() = _dailySummary

    init {
        val today = LocalDateTime.now()
        getDailySummaries(today.year.toString(), today.monthValue.toString(), today.dayOfMonth.toString())
        //getDailySummaries("2021","11","18")
    }


    fun getDailySummaries(year: String, month: String, day: String){
        uiScope.launch {
            _dailySummary.value = getDailySummariesAPI(year, month, day)
        }
    }

    private suspend fun getDailySummariesAPI(year: String, month: String, day: String): DailySummaryAPIResponse? {
        return withContext(Dispatchers.IO){

             SportAPI.getDailySummaries(year, month, day)

        }
    }
}