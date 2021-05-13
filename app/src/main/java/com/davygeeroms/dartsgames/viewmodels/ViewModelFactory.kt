package com.davygeeroms.dartsgames.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ViewModelFactory(private val application: Application): ViewModelProvider.Factory  {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(MainMenuViewModel::class.java)) {
            return MainMenuViewModel(application) as T
        }

        if (modelClass.isAssignableFrom(NewGameViewModel::class.java)) {
            return NewGameViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}