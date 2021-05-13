package com.davygeeroms.dartsgames.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.davygeeroms.dartsgames.R
import com.davygeeroms.dartsgames.databinding.FragmentMainMenuBinding
import com.davygeeroms.dartsgames.databinding.FragmentNewGameBinding
import com.davygeeroms.dartsgames.entities.Player
import com.davygeeroms.dartsgames.enums.GameModes
import com.davygeeroms.dartsgames.viewmodels.MainMenuViewModel
import com.davygeeroms.dartsgames.viewmodels.NewGameViewModel
import com.davygeeroms.dartsgames.viewmodels.ViewModelFactory

class NewGameFragment : Fragment() {

    private val vm: NewGameViewModel by activityViewModels()
    private lateinit var binding: FragmentNewGameBinding

    lateinit var vmFactory: ViewModelProvider.Factory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //application
        val application = requireNotNull(this.activity).application

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_new_game, container, false)

        //spinner
        var gameModes:MutableList<String> = mutableListOf<String>()
        enumValues<GameModes>().forEach { gameModes.add(it.mode) }

        val realmSpinner = binding.spinnerGameTypes
        var spinnerAdapter = this.context?.let { ArrayAdapter(it, R.layout.spinneritem,  gameModes) }
        if (spinnerAdapter != null) {
            spinnerAdapter.setDropDownViewResource(R.layout.spinnerdropdown)
            realmSpinner.adapter = spinnerAdapter
        }

        binding.butAddPlayer.setOnClickListener {
            vm.addPlayer(Player(binding.editTextPlayerName.text.toString()))
        }

        return binding.root
    }
}