package com.davygeeroms.dartsgames.fragments

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.davygeeroms.dartsgames.R
import com.davygeeroms.dartsgames.databinding.PlayGameFragmentBinding
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        //binding
        binding = DataBindingUtil.inflate(inflater, R.layout.play_game_fragment, container, false)

        pgvm.startGame(ngvm.selectedGameType, ngvm.players)

        binding.playerScore.setBackgroundColor(Color.parseColor(pgvm.currentGame.currentPlayer.color))
        binding.playerScore.text = pgvm.currentGame.currentScore.toString()
        binding.playerName.text = pgvm.currentGame.currentPlayer.name
        binding.playerNumber.text = pgvm.currentGame.currentPlayer.number.toString()


        mImageMap = binding.dartboardmapContainer.dartboardmap
        mImageMap.setImageResource(R.drawable.dartboard)

        //clickhandler imagebutton
        binding.dartboardButton.setOnClickListener {

            binding.dartboardmapContainer.visibility = View.VISIBLE

            //Imagemap clickhandler
            mImageMap.addOnImageMapClickedHandler(object : ImageMap.OnImageMapClickedHandler {

                var selectedArea : String? = null

                override fun onImageMapClicked(id: Int, imageMap: ImageMap?) {

                    mImageMap.showBubble(id)
                    selectedArea = mImageMap.getAreaName(id)
                    binding.dartboardmapContainer.visibility = View.GONE
                }

                override fun onBubbleClicked(id: Int) {

                }
            }
            )
        }



        return binding.root
    }
}