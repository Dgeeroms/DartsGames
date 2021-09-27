package com.davygeeroms.dartsgames.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.davygeeroms.dartsgames.persistence.GameDao

class ViewModelFactory(private val application: Application, private val gameDao: GameDao): ViewModelProvider.Factory  {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(MainMenuViewModel::class.java)) {
            return MainMenuViewModel(application) as T
        }

        if (modelClass.isAssignableFrom(NewGameViewModel::class.java)) {
            return NewGameViewModel(application, gameDao) as T
        }

        if (modelClass.isAssignableFrom(PlayGameViewModel::class.java)) {
            return PlayGameViewModel(application, gameDao) as T
        }

        if (modelClass.isAssignableFrom(WinnerViewModel::class.java)) {
            return WinnerViewModel(application, gameDao) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }

}