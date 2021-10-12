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
import com.davygeeroms.dartsgames.R
import com.davygeeroms.dartsgames.adapters.DeleteEndedGameListener
import com.davygeeroms.dartsgames.adapters.EndedGamesAdapter
import com.davygeeroms.dartsgames.databinding.ScoresFragmentBinding
import com.davygeeroms.dartsgames.persistence.AppDatabase
import com.davygeeroms.dartsgames.viewmodels.ScoresViewModel
import com.davygeeroms.dartsgames.viewmodels.ViewModelFactory

class ScoresFragment : Fragment() {

    private lateinit var vm: ScoresViewModel
    private lateinit var vmFactory: ViewModelProvider.Factory
    private lateinit var binding: ScoresFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //ActionBar titles
        (activity as AppCompatActivity).supportActionBar?.title = "DartsGames"
        (activity as AppCompatActivity).supportActionBar?.subtitle = "Game Scores"

        //application
        val application = requireNotNull(this.activity).application
        //appDB
        val appDB = AppDatabase.getInstance(application)

        vmFactory = ViewModelFactory(application, appDB.gameDao)
        vm = ViewModelProviders.of(this, vmFactory).get(ScoresViewModel::class.java)
        binding = DataBindingUtil.inflate(inflater, R.layout.scores_fragment, container, false)

        //recycler
        val recAdapter = EndedGamesAdapter( DeleteEndedGameListener { id ->  vm.deleteGame(id)})
        binding.rvEndedGames.adapter = recAdapter
        vm.gameList.observe(viewLifecycleOwner, Observer {
            recAdapter.submitList(it)
        })


        return binding.root
    }
}