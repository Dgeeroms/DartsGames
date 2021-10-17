package com.davygeeroms.dartsgames.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.toColorInt
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.davygeeroms.dartsgames.R
import com.davygeeroms.dartsgames.databinding.RecyclerongoinggamesitemBinding
import com.davygeeroms.dartsgames.databinding.RecyclerplayerhistoryitemBinding
import com.davygeeroms.dartsgames.entities.Game
import com.davygeeroms.dartsgames.entities.Turn
import com.davygeeroms.dartsgames.factories.BoardValueFactory
import com.davygeeroms.dartsgames.fragments.ContinueGameFragmentDirections
import com.davygeeroms.dartsgames.utilities.ColorInverter
import kotlinx.android.synthetic.main.recyclerplayerhistoryitem.view.*
import kotlinx.android.synthetic.main.recyclerplayersitem.view.*
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class TurnHistoryAdapter :
    ListAdapter<Turn, TurnHistoryAdapter.TurnHistoryViewHolder>(TurnHistoryDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TurnHistoryViewHolder {

        return TurnHistoryViewHolder.from(parent)

    }

    override fun onBindViewHolder(holder: TurnHistoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }



    class TurnHistoryViewHolder private constructor(val binding: RecyclerplayerhistoryitemBinding): RecyclerView.ViewHolder(binding.root){

        val playerName: TextView = binding.textViewPlayerName
        val playerCurrentScore: TextView = binding.textViewScoreValue
        val dart1Desc: TextView = binding.textViewScore1
        val dart2Desc: TextView = binding.textViewScore2
        val dart3Desc: TextView = binding.textViewScore3

        fun bind(
            item: Turn
        ) {
            //binding
            //binding.executePendingBindings()

            //reset visibility on thrown dart (if reused by recView they are still visible from last time)
            binding.lnlDart1.visibility = View.GONE
            binding.lnlDart2.visibility = View.GONE
            binding.lnlDart3.visibility = View.GONE

            

            //player name
            playerName.text = item.playerScore.player.name

            //set colors
            playerName.setTextColor(
                Color.parseColor(
                    ColorInverter.ColorInverter.invertColor(
                        item.playerScore.player.color
                    )
                )
            )
            val drawable = itemView.context.let {
                AppCompatResources.getDrawable(
                    it,
                    R.drawable.round_corners
                )
            }
            drawable?.setTint(Color.parseColor(item.playerScore.player.color))
            playerName.background = drawable


            //current score
            playerCurrentScore.text = item.playerScore.score.toString()

            //set colors
            playerCurrentScore.setTextColor(
                Color.parseColor(
                    ColorInverter.ColorInverter.invertColor(
                        item.playerScore.player.color
                    )
                )
            )
            val drawable2 = itemView.context.let {
                AppCompatResources.getDrawable(
                    it,
                    R.drawable.round_corners
                )
            }
            drawable2?.setTint(Color.parseColor(item.playerScore.player.color))
            playerCurrentScore.background = drawable2



            //list of thrown darts
            when(item.darts.count()){
                1 -> {
                    binding.lnlDart1.visibility = View.VISIBLE
                    dart1Desc.text = item.darts[0].description
                }
                2 -> {
                    binding.lnlDart1.visibility = View.VISIBLE
                    dart1Desc.text = item.darts[0].description
                    binding.lnlDart2.visibility = View.VISIBLE
                    dart2Desc.text = item.darts[1].description
                }
                3 -> {
                    binding.lnlDart1.visibility = View.VISIBLE
                    dart1Desc.text = item.darts[0].description
                    binding.lnlDart2.visibility = View.VISIBLE
                    dart2Desc.text = item.darts[1].description
                    binding.lnlDart3.visibility = View.VISIBLE
                    dart3Desc.text = item.darts[2].description
                }
            }
        }

        companion object {
            fun from(parent: ViewGroup): TurnHistoryViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RecyclerplayerhistoryitemBinding.inflate(layoutInflater, parent, false)
                return TurnHistoryViewHolder(binding)
            }
        }
    }
}

class TurnHistoryDiffCallback: DiffUtil.ItemCallback<Turn>(){
    override fun areItemsTheSame(oldItem: Turn, newItem: Turn): Boolean {
        return oldItem.timeStamp == newItem.timeStamp
    }

    override fun areContentsTheSame(oldItem: Turn, newItem: Turn): Boolean {
        return oldItem == newItem
    }
}


