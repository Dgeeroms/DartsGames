package com.davygeeroms.dartsgames

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.davygeeroms.dartsgames.viewmodels.PlayGameViewModel
import com.davygeeroms.dartsgames.viewmodels.MainMenuViewModel
import com.davygeeroms.dartsgames.viewmodels.NewGameViewModel

class MainActivity : AppCompatActivity() {

    private val newGameViewModel: NewGameViewModel by viewModels()
    private val mainMenuViewModel: MainMenuViewModel by viewModels()
    private val playGameViewModel: PlayGameViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}