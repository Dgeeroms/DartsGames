package com.davygeeroms.dartsgames.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.davygeeroms.dartsgames.R
import com.davygeeroms.dartsgames.databinding.RecyclerstatisticsitemBinding
import com.davygeeroms.dartsgames.entities.PlayerScore
import com.davygeeroms.dartsgames.utilities.ColorInverter

class StatisticsAdapter :
    ListAdapter<PlayerScore, StatisticsAdapter.StatisticsViewHolder>(StatisticsAdapterDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):StatisticsViewHolder {

        return StatisticsViewHolder.from(parent)

    }

    override fun onBindViewHolder(holder: StatisticsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }



    class StatisticsViewHolder private constructor(val binding: RecyclerstatisticsitemBinding): RecyclerView.ViewHolder(binding.root){

        val playerName: TextView = binding.lblPlayerValue
        val avgScore: TextView = binding.lblAverageScoreValue
        val highestThrown: TextView = binding.lblHighestThrownValue
        val dartsThrown: TextView = binding.lblNumberDartsThrownValue
        val dartsHit: TextView = binding.lblNumberDartsHitValue
        val dartsMiss: TextView = binding.lblNumberDartsMissValue
        val dartsHitPct: TextView = binding.lblHitPercentageValue
        val dartsMissPct: TextView = binding.lblMissPercentageValue
        val checkOutScore: TextView = binding.lblCheckOutValue
        val lblCheckOutScore: TextView = binding.lblCheckOut

        fun bind(
            item: PlayerScore
        ) {

            //miss percentage
            var string = item.stat.getMissPct().toString() + "%"
            dartsMissPct.text = string

            //hit percentage
            string = item.stat.getHitPct().toString() + "%"
            dartsHitPct.text = string

            //darts missed
            dartsMiss.text = item.stat.misses.toString()

            //darts hit
            dartsHit.text = item.stat.hits.toString()

            //dartsThrown
            dartsThrown.text = item.stat.totalThrows.toString()

            //highestThrow
            highestThrown.text = item.stat.highestCheckOut.toString()

            //avg
            avgScore.text = item.stat.getAvg().toString()


            //player name
            playerName.text = item.player.name

            //set colors
            playerName.setTextColor(
                Color.parseColor(
                    ColorInverter.ColorInverter.invertColor(
                        item.player.color
                    )
                )
            )
            val drawable = itemView.context.let {
                AppCompatResources.getDrawable(
                    it,
                    R.drawable.round_corners_small
                )
            }
            drawable?.setTint(Color.parseColor(item.player.color))
            playerName.background = drawable


            //checkout
            checkOutScore.visibility = View.GONE
            lblCheckOutScore.visibility = View.GONE
            if(item.stat.highestCheckOut != 0){
                checkOutScore.text = item.stat.highestCheckOut.toString()
                checkOutScore.visibility = View.VISIBLE
                lblCheckOutScore.visibility = View.VISIBLE
            }

        }

        companion object {
            fun from(parent: ViewGroup): StatisticsViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RecyclerstatisticsitemBinding.inflate(layoutInflater, parent, false)
                return StatisticsViewHolder(binding)
            }
        }
    }
}

class StatisticsAdapterDiffCallback: DiffUtil.ItemCallback<PlayerScore>(){
    override fun areItemsTheSame(oldItem: PlayerScore, newItem: PlayerScore): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: PlayerScore, newItem: PlayerScore): Boolean {
        return oldItem == newItem
    }
}


