package com.davygeeroms.dartsgames.fragments

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.davygeeroms.dartsgames.R
import com.davygeeroms.dartsgames.adapters.PlayerHistoryAdapter
import com.davygeeroms.dartsgames.databinding.PlayGameFragmentBinding
import com.davygeeroms.dartsgames.entities.PlayerScoreHistory
import com.davygeeroms.dartsgames.persistence.AppDatabase
import com.davygeeroms.dartsgames.utilities.ImageMap
import com.davygeeroms.dartsgames.viewmodels.PlayGameViewModel
import com.davygeeroms.dartsgames.viewmodels.ViewModelFactory
import kotlinx.android.synthetic.main.dartboardmap_container.view.*

class PlayGameFragment : Fragment() {

    private lateinit var vm: PlayGameViewModel
    private lateinit var vmFactory: ViewModelProvider.Factory
    private lateinit var binding: PlayGameFragmentBinding
    private lateinit var mImageMap: ImageMap
    private lateinit var playerScoreHistoryRecyclerView: RecyclerView
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var playerScoreHistoryAdapter: PlayerHistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        //application
        val application = requireNotNull(this.activity).application

        val args = PlayGameFragmentArgs.fromBundle(requireArguments())

        //binding
        binding = DataBindingUtil.inflate(inflater, R.layout.play_game_fragment, container, false)

        //appDB
        val appDB = AppDatabase.getInstance(application)
        //ViewModel
        vmFactory = ViewModelFactory(application, appDB.gameDao)
        vm = ViewModelProviders.of(this, vmFactory).get(PlayGameViewModel::class.java)

        vm.continueGame(args.gameId)

        vm.currentGame.observe(viewLifecycleOwner, Observer { game ->

            binding.playerScore.setBackgroundColor(Color.parseColor(game.currentPlayer.color))
            binding.playerScore.text = game.displayedString
            binding.playerName.text = game.currentPlayer.name
            binding.playerNumber.text = game.currentPlayer.number.toString()
            vm.updateNewGameStatus()
            var recInitialized = false
            if(game.playerScoreHistory.count() > 0 && !recInitialized){
                initializeRec()
                recInitialized = true
            }

            if(game.dartNumber == 1 && game.playerScores.count() > 1){
                view?.let { showNextPlayerDialog(it) }
            }

        })


        mImageMap = binding.dartboardmapContainer.dartboardmap
        mImageMap.setImageResource(R.drawable.dartboard)




        //clickhandler imagebutton
        binding.dartboardButton.setOnClickListener {

            binding.dartboardmapContainer.visibility = View.VISIBLE

            //Imagemap clickhandler
            mImageMap.addOnImageMapClickedHandler(object : ImageMap.OnImageMapClickedHandler {

                lateinit var selectedArea : String

                override fun onImageMapClicked(id: Int, imageMap: ImageMap?) {

                    selectedArea = mImageMap.getAreaName(id)
                    vm.throwDart(selectedArea.substring(0, 3))
                    mImageMap.removeClickHandlers()
                    binding.dartboardmapContainer.visibility = View.GONE
                }

                override fun onBubbleClicked(id: Int) {

                }
            })
        }

        //recycler initialized with empty list
        playerScoreHistoryRecyclerView = binding.playerHistoryRec
        linearLayoutManager = LinearLayoutManager(this.context)
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true
        playerScoreHistoryRecyclerView.layoutManager = linearLayoutManager
        playerScoreHistoryAdapter = PlayerHistoryAdapter(ArrayList<PlayerScoreHistory>())
        playerScoreHistoryRecyclerView.adapter = playerScoreHistoryAdapter

        return binding.root
    }

    private fun initializeRec() {
        playerScoreHistoryRecyclerView = binding.playerHistoryRec
        linearLayoutManager = LinearLayoutManager(this.context)
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true
        playerScoreHistoryRecyclerView.layoutManager = linearLayoutManager
        val playerScoreHistories = vm.currentGame.value?.playerScoreHistory
        playerScoreHistoryAdapter = PlayerHistoryAdapter(playerScoreHistories as ArrayList<PlayerScoreHistory>)
        playerScoreHistoryRecyclerView.adapter = playerScoreHistoryAdapter
    }

    private fun showNextPlayerDialog(view: View){
        vm.currentGame.value?.currentPlayer?.name?.let { NextPlayerDialogFragment(it).show(parentFragmentManager, "com.davygeeroms.dartsgames.fragments.NextPlayerDialogFragment") }
    }

}