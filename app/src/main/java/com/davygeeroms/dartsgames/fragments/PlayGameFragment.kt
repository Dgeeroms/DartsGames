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
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.davygeeroms.dartsgames.R
import com.davygeeroms.dartsgames.adapters.PlayerHistoryAdapter
import com.davygeeroms.dartsgames.databinding.PlayGameFragmentBinding
import com.davygeeroms.dartsgames.entities.PlayerScoreHistory
import com.davygeeroms.dartsgames.persistence.AppDatabase
import com.davygeeroms.dartsgames.utilities.ColorInverter
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

    override fun onStop() {
        super.onStop()
        vm.saveGame()
    }


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

        //observe undoableThrow, last throw will be kept in memory so it can be undone
        binding.btnUndoThrow.visibility = View.INVISIBLE
        binding.btnUndoThrow.animate().translationX(-1000F)
        vm.undoableThrow.observe(viewLifecycleOwner, Observer { udThrow ->
            if(udThrow == null){
                binding.btnUndoThrow.animate().translationX(-1000F)
            } else {
                binding.btnUndoThrow.visibility = View.VISIBLE
                binding.btnUndoThrow.animate().translationX(0F)
            }
        })

        //undo last throw btn click listener
        binding.btnUndoThrow.setOnClickListener {
            vm.undoLastThrow()
            updateRec()
        }

        //missed dart btn click listener
        binding.btnMissThrow.setOnClickListener {
            vm.throwDart("S00")
        }

        //observe if checkout is possible
        binding.checkoutAvailable.animate().translationY(-1000F)
        vm.checkOutTable.observe(viewLifecycleOwner, Observer { cot ->
            if(cot == null){
                binding.checkoutAvailable.animate().translationY(-1000F)
            } else {
                binding.checkoutAvailable.visibility = View.VISIBLE
                binding.checkoutAvailable.animate().translationY(0F)
            }
        })

        // set listener on checkouts btn
        binding.checkoutAvailable.setOnClickListener {
            showCheckOutsDialog(it)
        }

        //observe current game
        vm.currentGame.observe(viewLifecycleOwner, Observer { game ->

            binding.playerScore.setBackgroundColor(Color.parseColor(game.currentPlayer.color))
            binding.playerScore.setTextColor(Color.parseColor(ColorInverter.ColorInverter.invertColor(game.currentPlayer.color)))
            binding.playerScore.text = game.displayedString

            val nowPlayingString = "Player ${game.currentPlayer.number}: ${game.currentPlayer.name}"
            binding.playerName.text = nowPlayingString
            binding.playerName.setBackgroundColor(Color.parseColor(game.currentPlayer.color))
            binding.playerName.setTextColor(Color.parseColor(ColorInverter.ColorInverter.invertColor(game.currentPlayer.color)))

            val dartNumberString = "Dart: ${game.dartNumber}"
            binding.dartNumber.text = dartNumberString
            binding.dartNumber.setBackgroundColor(Color.parseColor(game.currentPlayer.color))
            binding.dartNumber.setTextColor(Color.parseColor(ColorInverter.ColorInverter.invertColor(game.currentPlayer.color)))

            vm.updateNewGameStatus()

            updateRec()

            if(game.dartNumber == 1 && game.playerScores.count() > 1 && !game.hasWon && game.currentPlayer.number != vm.undoableThrow.value?.playerScore?.player?.number){
                view?.let { showNextPlayerDialog(it) }
            }

            if(game.hasWon){
                view?.findNavController()?.navigate(PlayGameFragmentDirections.actionPlayGameFragmentToWinnerFragment(game.id))
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
        playerScoreHistoryAdapter = PlayerHistoryAdapter()
        playerScoreHistoryAdapter.dataSet = listOf()
        playerScoreHistoryRecyclerView.adapter = playerScoreHistoryAdapter

        return binding.root
    }

    private fun updateRec() {
        playerScoreHistoryRecyclerView = binding.playerHistoryRec

        val playerScoreHistories = vm.currentGame.value?.playerScoreHistory
        playerScoreHistoryAdapter = PlayerHistoryAdapter()
        if (playerScoreHistories != null) {
            playerScoreHistoryAdapter.dataSet = playerScoreHistories
        }
        playerScoreHistoryRecyclerView.adapter = playerScoreHistoryAdapter
    }


    private fun showNextPlayerDialog(view: View){
        vm.currentGame.value?.currentPlayer?.let { NextPlayerDialogFragment(it).show(parentFragmentManager, "com.davygeeroms.dartsgames.fragments.NextPlayerDialogFragment") }
    }

    private fun showCheckOutsDialog(view: View){
        CheckOutsDialogFragment(vm.checkOutTable.value!!).show(parentFragmentManager, "com.davygeeroms.dartsgames.fragments.CheckOutsDialogFragment")
    }

}