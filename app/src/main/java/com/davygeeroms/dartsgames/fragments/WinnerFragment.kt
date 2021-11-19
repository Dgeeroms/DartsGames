package com.davygeeroms.dartsgames.fragments

import android.content.Context
import android.graphics.Color
import android.icu.text.DateTimePatternGenerator
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.marginLeft
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.viewpager.widget.ViewPager
import com.davygeeroms.dartsgames.R
import com.davygeeroms.dartsgames.adapters.DeleteGameListener
import com.davygeeroms.dartsgames.adapters.OngoingGamesAdapter
import com.davygeeroms.dartsgames.adapters.StatisticsAdapter
import com.davygeeroms.dartsgames.databinding.FragmentWinnerBinding
import com.davygeeroms.dartsgames.persistence.AppDatabase
import com.davygeeroms.dartsgames.utilities.ColorInverter
import com.davygeeroms.dartsgames.viewmodels.ViewModelFactory
import com.davygeeroms.dartsgames.viewmodels.WinnerViewModel

class WinnerFragment : Fragment() {

    private lateinit var binding: FragmentWinnerBinding
    private lateinit var vm: WinnerViewModel
    private lateinit var vmFactory: ViewModelProvider.Factory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        //ActionBar titles
        (activity as AppCompatActivity).supportActionBar?.title = "DartsGames"
        (activity as AppCompatActivity).supportActionBar?.subtitle = "Winner, Winner, chicken dinner!"

        //args
        val args = PlayGameFragmentArgs.fromBundle(requireArguments())

        //application
        val application = requireNotNull(this.activity).application

        //binding
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_winner, container, false)

        //appDB
        val appDB = AppDatabase.getInstance(application)

        //ViewModel
        vmFactory = ViewModelFactory(application, appDB.gameDao)
        vm = ViewModelProviders.of(this, vmFactory).get(WinnerViewModel::class.java)
        vm.getFinalizedGame(args.gameId)


        vm.currentGame.observe(viewLifecycleOwner, Observer { game ->
            if(game != null){
                val winnerText = "Winner:\n${game.currentTurn.playerScore.player.name}!!"
                binding.lblWinningPlayer.text = winnerText
                val layoutParam = binding.lblWinningPlayer.layoutParams
                layoutParam.width = ViewPager.LayoutParams.MATCH_PARENT
                binding.lblWinningPlayer.layoutParams = layoutParam
                binding.lblWinningPlayer.setBackgroundColor(Color.parseColor(game.currentTurn.playerScore.player.color))
                binding.lblWinningPlayer.setTextColor(Color.parseColor(ColorInverter.ColorInverter.invertColor(game.currentTurn.playerScore.player.color)))

            }
        })

        //recycler
        val recAdapter = StatisticsAdapter()
        binding.rvStats.adapter = recAdapter
        vm.currentGame.observe(viewLifecycleOwner, Observer {
            recAdapter.submitList(it.playerScores)
        })

        return binding.root
    }

}
