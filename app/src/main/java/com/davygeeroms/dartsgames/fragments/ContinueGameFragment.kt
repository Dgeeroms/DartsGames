package com.davygeeroms.dartsgames.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.davygeeroms.dartsgames.R
import com.davygeeroms.dartsgames.adapters.DeleteGameListener
import com.davygeeroms.dartsgames.adapters.OngoingGamesAdapter
import com.davygeeroms.dartsgames.databinding.ContinueGameFragmentBinding
import com.davygeeroms.dartsgames.databinding.FragmentMainMenuBinding
import com.davygeeroms.dartsgames.persistence.AppDatabase
import com.davygeeroms.dartsgames.viewmodels.ContinueGameViewModel
import com.davygeeroms.dartsgames.viewmodels.MainMenuViewModel
import com.davygeeroms.dartsgames.viewmodels.ViewModelFactory

class ContinueGameFragment : Fragment() {

    private lateinit var vm: ContinueGameViewModel
    private lateinit var binding: ContinueGameFragmentBinding
    private lateinit var vmFactory: ViewModelProvider.Factory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        //ActionBar titles
        (activity as AppCompatActivity).supportActionBar?.title = "DartsGames"
        (activity as AppCompatActivity).supportActionBar?.subtitle = "Continue Game"

        //application
        val application = requireNotNull(this.activity).application
        //appDB
        val appDB = AppDatabase.getInstance(application)

        vmFactory = ViewModelFactory(application, appDB.gameDao)
        vm = ViewModelProviders.of(this, vmFactory).get(ContinueGameViewModel::class.java)
        binding = DataBindingUtil.inflate(inflater, R.layout.continue_game_fragment, container, false)

        //recycler
        val recAdapter = OngoingGamesAdapter( DeleteGameListener { id ->  vm.deleteGame(id)})
        binding.rvOngoingGames.adapter = recAdapter
        vm.gameList.observe(viewLifecycleOwner, Observer {
            recAdapter.submitList(it)
        })





        return binding.root
    }



}