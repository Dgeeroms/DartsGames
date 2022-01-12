package com.davygeeroms.dartsgames.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.davygeeroms.dartsgames.R
import com.davygeeroms.dartsgames.adapters.TournamentFeedAdapter
import com.davygeeroms.dartsgames.databinding.FragmentMainMenuBinding
import com.davygeeroms.dartsgames.persistence.AppDatabase
import com.davygeeroms.dartsgames.viewmodels.MainMenuViewModel
import com.davygeeroms.dartsgames.viewmodels.ViewModelFactory
import java.time.LocalDateTime
import java.util.*


class MainMenuFragment : Fragment() {


    private lateinit var binding: FragmentMainMenuBinding
    private lateinit var vm: MainMenuViewModel
    private lateinit var vmFactory: ViewModelProvider.Factory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        //ActionBar titles
        (activity as AppCompatActivity).supportActionBar?.title = "DartsGames"
        (activity as AppCompatActivity).supportActionBar?.subtitle = "Main Menu"


        //application
        val application = requireNotNull(this.activity).application
        //appDB
        val appDB = AppDatabase.getInstance(application)

        vmFactory = ViewModelFactory(application, appDB.gameDao)
        vm = ViewModelProviders.of(this, vmFactory).get(MainMenuViewModel::class.java)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main_menu, container, false)

        val recAdapter = TournamentFeedAdapter()
        binding.rvTournaments.adapter = recAdapter
        vm.dailySummary.observe(viewLifecycleOwner, Observer {
            if(it != null){
                recAdapter.submitList(it.dailySummaries)
            }
        })


        binding.butNewGame.setOnClickListener {
            view?.findNavController()?.navigate(MainMenuFragmentDirections.actionMainMenuFragmentToNewGameFragment())
        }

        binding.butContinueGame.setOnClickListener {
            view?.findNavController()?.navigate(MainMenuFragmentDirections.actionMainMenuFragmentToContinueGameFragment())
        }

        binding.butScores.setOnClickListener {
            view?.findNavController()?.navigate(MainMenuFragmentDirections.actionMainMenuFragmentToScoresFragment())
        }

        //binding.datePicker.maxDate = System.currentTimeMillis() - 1000
        binding.refresh.setOnClickListener {
            vm.getDailySummaries(binding.datePicker.year.toString(), (binding.datePicker.month + 1) , binding.datePicker.dayOfMonth)
        }

        return binding.root
    }

}