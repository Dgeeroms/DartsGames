package com.davygeeroms.dartsgames.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.davygeeroms.dartsgames.R
import com.davygeeroms.dartsgames.databinding.RecyclerendedgamesitemBinding
import com.davygeeroms.dartsgames.entities.Game
import com.davygeeroms.dartsgames.fragments.ScoresFragmentDirections
import com.davygeeroms.dartsgames.utilities.ColorInverter
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter


class EndedGamesAdapter(val clickListener: DeleteEndedGameListener)
    : ListAdapter<Game, EndedGamesAdapter.EndedGamesViewHolder>(EndedGamesDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EndedGamesViewHolder {

        return EndedGamesViewHolder.from(parent)

    }

    override fun onBindViewHolder(holder: EndedGamesViewHolder, position: Int) {
        holder.bind(getItem(position), clickListener)
    }



    class EndedGamesViewHolder private constructor(val binding: RecyclerendedgamesitemBinding): RecyclerView.ViewHolder(binding.root){
        val timeStamp: TextView = binding.timeStampEnded
        val leadingPlayer: TextView = binding.lblWinningPlayer
        val leadingPlayerScore: TextView = binding.lblEndedScoreValue
        val deleteIcon: ImageView = binding.deleteEndedIcon
        val continueIcon: ImageView = binding.endedGameIcon
        val gameType: TextView = binding.lblEndedGameTypeValue

        fun bind(
            item: Game,
            clickListener: DeleteEndedGameListener
        ) {
            //binding
            binding.game = item
            binding.clickListener = clickListener
            binding.executePendingBindings()

            //timestamp
            val formatter =
                DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm").withZone(ZoneId.from(ZoneOffset.UTC))
            timeStamp.text = formatter.format(item.startTime)

            //players from low to high
            val playerScores = item.playerScores.sortedBy { it.score }

            //lowest player is in the lead
            leadingPlayer.text = playerScores.first().player.name
            //set player colors
            leadingPlayer.setTextColor(
                Color.parseColor(
                    ColorInverter.ColorInverter.invertColor(
                        playerScores.first().player.color
                    )
                )
            )
            val drawable = itemView.context.let {
                AppCompatResources.getDrawable(
                    it,
                    R.drawable.round_corners_small
                )
            }
            drawable?.setTint(Color.parseColor(playerScores.first().player.color))
            leadingPlayer.background = drawable

            //set gameType
            gameType.text = item.gameType.gameMode.mode

            //leading player score
            leadingPlayerScore.text = playerScores.first().score.toString()

            //onClickListener continue game
            continueIcon.setOnClickListener {
                itemView.findNavController().navigate(
                    ScoresFragmentDirections.actionScoresFragmentToWinnerFragment(item.id)
                )
            }
        }

        companion object {
            fun from(parent: ViewGroup): EndedGamesViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RecyclerendedgamesitemBinding.inflate(layoutInflater, parent, false)
                return EndedGamesViewHolder(binding)
            }
        }
    }
}

class EndedGamesDiffCallback: DiffUtil.ItemCallback<Game>(){
    override fun areItemsTheSame(oldItem: Game, newItem: Game): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Game, newItem: Game): Boolean {
        return oldItem == newItem
    }
}

class DeleteEndedGameListener(val clickListener: (id: Int) -> Unit){
    fun onClick(game: Game) = clickListener(game.id)
}