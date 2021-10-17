package com.davygeeroms.dartsgames.fragments

import android.graphics.Color
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.davygeeroms.dartsgames.R
import com.davygeeroms.dartsgames.adapters.TurnHistoryAdapter
import com.davygeeroms.dartsgames.databinding.PlayGameFragmentBinding
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

    override fun onStop() {
        super.onStop()
        vm.saveGame()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        //ActionBar titles

        (activity as AppCompatActivity).supportActionBar?.title = "DartsGames"
        (activity as AppCompatActivity).supportActionBar?.subtitle = "Game on!"


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

        //undoableThrow button initially offscreen
        binding.btnUndoThrow.visibility = View.INVISIBLE
        binding.btnUndoThrow.animate().translationX(-1000F)

        //undo last throw btn click listener
        binding.btnUndoThrow.setOnClickListener {
            vm.undoLastThrow()
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

        //recView
        val recAdapter = TurnHistoryAdapter()
        binding.playerHistoryRec.adapter = recAdapter

        //observe if there's previous throw available
        vm.previousThrow.observe(viewLifecycleOwner, Observer {
            prevThrow ->
                if(prevThrow == null){
                    binding.btnUndoThrow.animate().translationX(-1000F)
                } else {
                    binding.btnUndoThrow.visibility = View.VISIBLE
                    binding.btnUndoThrow.animate().translationX(0F)
                }
        })

        //observe current game
        vm.currentGame.observe(viewLifecycleOwner, Observer { game ->

            recAdapter.submitList(game.playerScoreHistory)
            recAdapter.notifyItemChanged(game.playerScoreHistory.lastIndex)

            binding.playerScore.setBackgroundColor(Color.parseColor(game.currentTurn.playerScore.player.color))
            binding.playerScore.setTextColor(Color.parseColor(ColorInverter.ColorInverter.invertColor(game.currentTurn.playerScore.player.color)))
            binding.playerScore.text = game.displayedScoreString

            val nowPlayingString = "Player ${game.currentTurn.playerScore.player.number}: ${game.currentTurn.playerScore.player.name}"
            binding.playerName.text = nowPlayingString
            binding.playerName.setBackgroundColor(Color.parseColor(game.currentTurn.playerScore.player.color))
            binding.playerName.setTextColor(Color.parseColor(ColorInverter.ColorInverter.invertColor(game.currentTurn.playerScore.player.color)))

            val dartNumberString = "Dart: ${game.dartNumber}"
            binding.dartNumber.text = dartNumberString
            binding.dartNumber.setBackgroundColor(Color.parseColor(game.currentTurn.playerScore.player.color))
            binding.dartNumber.setTextColor(Color.parseColor(ColorInverter.ColorInverter.invertColor(game.currentTurn.playerScore.player.color)))

            vm.updateNewGameStatus()

            if(game.dartNumber == 1
                && game.playerScores.count() > 1
                && !game.hasWon){
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
                    if(selectedArea != "XXX"){
                        vm.throwDart(selectedArea.substring(0, 3))
                    }
                    mImageMap.removeClickHandlers()
                    binding.dartboardmapContainer.visibility = View.GONE
                }

                override fun onBubbleClicked(id: Int) {

                }
            })
        }

        return binding.root
    }


    private fun showNextPlayerDialog(view: View){
        vm.currentGame.value?.currentTurn?.playerScore?.player?.let { NextPlayerDialogFragment(it).show(parentFragmentManager, "com.davygeeroms.dartsgames.fragments.NextPlayerDialogFragment") }
    }

    private fun showCheckOutsDialog(view: View){
        CheckOutsDialogFragment(vm.checkOutTable.value!!).show(parentFragmentManager, "com.davygeeroms.dartsgames.fragments.CheckOutsDialogFragment")
    }

}