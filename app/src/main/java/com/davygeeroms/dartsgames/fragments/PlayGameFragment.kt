package com.davygeeroms.dartsgames.fragments

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.davygeeroms.dartsgames.R
import com.davygeeroms.dartsgames.adapters.PlayerHistoryAdapter
import com.davygeeroms.dartsgames.adapters.PlayersAdapter
import com.davygeeroms.dartsgames.databinding.PlayGameFragmentBinding
import com.davygeeroms.dartsgames.entities.Player
import com.davygeeroms.dartsgames.entities.PlayerScoreHistory
import com.davygeeroms.dartsgames.utilities.ImageMap
import com.davygeeroms.dartsgames.viewmodels.NewGameViewModel
import com.davygeeroms.dartsgames.viewmodels.PlayGameViewModel
import kotlinx.android.synthetic.main.dartboardmap_container.view.*
import kotlinx.android.synthetic.main.play_game_fragment.view.*

class PlayGameFragment : Fragment() {

    private val pgvm: PlayGameViewModel by activityViewModels()
    private val ngvm: NewGameViewModel by activityViewModels()
    private lateinit var binding: PlayGameFragmentBinding
    private lateinit var mImageMap: ImageMap
    private lateinit var playerScoreHistoryRecyclerView: RecyclerView
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var playerScoreHistoryAdapter: PlayerHistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        //binding
        binding = DataBindingUtil.inflate(inflater, R.layout.play_game_fragment, container, false)

        pgvm.startGame(ngvm.selectedGameType, ngvm.players.value!!)

        pgvm.currentGame.observe(viewLifecycleOwner, Observer { game ->

            binding.playerScore.setBackgroundColor(Color.parseColor(game.currentPlayer.color))
            binding.playerScore.text = game.currentScore.toString()
            binding.playerName.text = game.currentPlayer.name
            binding.playerNumber.text = game.currentPlayer.number.toString()

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
                    pgvm.throwDart(selectedArea.substring(0, 3))
                    mImageMap.removeClickHandlers()
                    binding.dartboardmapContainer.visibility = View.GONE
                }

                override fun onBubbleClicked(id: Int) {

                }
            })
        }

        //recycler
        playerScoreHistoryRecyclerView = binding.playerHistoryRec
        linearLayoutManager = LinearLayoutManager(this.context)
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true
        playerScoreHistoryRecyclerView.layoutManager = linearLayoutManager
        val playerScoreHistories = pgvm.currentGame.value?.playerScoreHistory
        playerScoreHistoryAdapter = PlayerHistoryAdapter(playerScoreHistories as ArrayList<PlayerScoreHistory>)
        playerScoreHistoryRecyclerView.adapter = playerScoreHistoryAdapter



        return binding.root
    }
}