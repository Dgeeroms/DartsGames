package com.davygeeroms.dartsgames.adapters

import android.R
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.davygeeroms.dartsgames.databinding.RecyclermaintournamentitemBinding
import com.davygeeroms.dartsgames.entities.sportradarAPIResponse.DailySummary
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class TournamentFeedAdapter()
    : ListAdapter<DailySummary, TournamentFeedAdapter.DailySummaryViewHolder>(DailySummaryDiffCallback()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailySummaryViewHolder {

        return DailySummaryViewHolder.from(parent)

    }

    override fun onBindViewHolder(holder: DailySummaryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DailySummaryViewHolder private constructor(val binding: RecyclermaintournamentitemBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(
            item: DailySummary
        ) {
            //binding
            binding.executePendingBindings()

            //competition
            val competition = binding.lblCompetitionValue
            val season = binding.lblSeasonValue
            val matchStatus = binding.lblMatchStatusValue
            val startTime = binding.startTimeValue

            //player 1
            val player1 = binding.player1
            val fullNamePlay1 = binding.lblComp1Value
            val avg1 = binding.lblAverageScoreValue1
            val checkoutPct1 = binding.lblCheckoutPercentageValue1
            val checkouts1 = binding.lblCheckoutsValue1
            val checkoutsAbove100_1 = binding.lblCheckoutsPlus100Value1
            val highestCO1 = binding.lblHighestCheckoutValue1
            val scoreAbove100_1 = binding.lblScoresPlus100Value1
            val scoreAbove140_1 = binding.lblScoresPlus140Value1
            val oneEighties1 = binding.lblOneEightiesValue1

            //player 2
            val player2 = binding.player2
            val fullNamePlay2 = binding.lblComp2Value
            val avg2 = binding.lblAverageScoreValue2
            val checkoutPct2 = binding.lblCheckoutPercentageValue2
            val checkouts2 = binding.lblCheckoutsValue2
            val checkoutsAbove100_2 = binding.lblCheckoutsPlus100Value2
            val highestCO2 = binding.lblHighestCheckoutValue2
            val scoreAbove100_2 = binding.lblScoresPlus100Value2
            val scoreAbove140_2 = binding.lblScoresPlus140Value2
            val oneEighties2 = binding.lblOneEightiesValue2

            //expand arrow
            val arrow : ImageView = binding.expandData

            //competition values
            competition.text = item.sportEvent.sportEventContext.competition.name
            season.text = item.sportEvent.sportEventContext.season.name
            matchStatus.text = item.sportEventStatus.matchStatus

            val startTimeVal = item.sportEvent.startTime
            val dt = OffsetDateTime.parse(startTimeVal)
            val dateString = "${dt.dayOfMonth}/${dt.monthValue}/${dt.year} - ${dt.hour}:${dt.minute}"
            startTime.text = dateString

            //toggle stats off by default
            toggleStats(false)
            var toggleState = false

            var string = item.sportEvent.competitors[0].name + " (" + item.sportEvent.competitors[0].abbreviation + ")"
            fullNamePlay1.text = string
            string = item.sportEvent.competitors[1].name + " (" + item.sportEvent.competitors[1].abbreviation + ")"
            fullNamePlay2.text = string

            if(item.statistics != null) {

                arrow.visibility = View.VISIBLE

                //player 1 values
                val comp1 = item.statistics.totals.competitorStats[0]
                player1.text = comp1.abbreviation
                avg1.text = comp1.statisticsDetails.avgThreeDarts.toString()
                checkoutPct1.text = comp1.statisticsDetails.checkoutPercentage.toString()
                checkouts1.text = comp1.statisticsDetails.checkouts.toString()
                checkoutsAbove100_1.text = comp1.statisticsDetails.checkout100plus.toString()
                highestCO1.text = comp1.statisticsDetails.highestCheckout.toString()
                scoreAbove100_1.text = comp1.statisticsDetails.times100plus.toString()
                scoreAbove140_1.text = comp1.statisticsDetails.times140plus.toString()
                oneEighties1.text = comp1.statisticsDetails.oneEighties.toString()

                //player 2 values
                val comp2 = item.statistics.totals.competitorStats[1]
                player2.text = comp2.abbreviation
                avg2.text = comp2.statisticsDetails.avgThreeDarts.toString()
                checkoutPct2.text = comp2.statisticsDetails.checkoutPercentage.toString()
                checkouts2.text = comp2.statisticsDetails.checkouts.toString()
                checkoutsAbove100_2.text = comp2.statisticsDetails.checkout100plus.toString()
                highestCO2.text = comp2.statisticsDetails.highestCheckout.toString()
                scoreAbove100_2.text = comp2.statisticsDetails.times100plus.toString()
                scoreAbove140_2.text = comp2.statisticsDetails.times140plus.toString()
                oneEighties2.text = comp2.statisticsDetails.oneEighties.toString()

            } else {
                arrow.visibility = View.GONE
            }

            arrow.setOnClickListener {
                toggleState = !toggleState
                toggleStats(toggleState)
            }

        }

        fun toggleStats(visible: Boolean) {

            val arrow = binding.expandData
            arrow.rotation = 90F

            var visibility: Int = View.VISIBLE
            if (visible) {
                visibility = View.VISIBLE
                arrow.rotation = 0F
            } else {
                visibility = View.GONE
                arrow.rotation = 180F
            }


            //player 1
            val player1 = binding.player1
            val avg1 = binding.lblAverageScoreValue1
            val checkoutPct1 = binding.lblCheckoutPercentageValue1
            val checkouts1 = binding.lblCheckoutsValue1
            val checkoutsAbove100_1 = binding.lblCheckoutsPlus100Value1
            val highestCO1 = binding.lblHighestCheckoutValue1
            val scoreAbove100_1 = binding.lblScoresPlus100Value1
            val scoreAbove140_1 = binding.lblScoresPlus140Value1
            val oneEighties1 = binding.lblOneEightiesValue1

            //player 2
            val player2 = binding.player2
            val avg2 = binding.lblAverageScoreValue2
            val checkoutPct2 = binding.lblCheckoutPercentageValue2
            val checkouts2 = binding.lblCheckoutsValue2
            val checkoutsAbove100_2 = binding.lblCheckoutsPlus100Value2
            val highestCO2 = binding.lblHighestCheckoutValue2
            val scoreAbove100_2 = binding.lblScoresPlus100Value2
            val scoreAbove140_2 = binding.lblScoresPlus140Value2
            val oneEighties2 = binding.lblOneEightiesValue2

            player1.visibility = visibility
            avg1.visibility = visibility
            binding.lblAverageScore1.visibility = visibility
            checkoutPct1.visibility = visibility
            binding.checkoutPercentage1.visibility = visibility
            checkouts1.visibility = visibility
            binding.lblCheckouts1.visibility = visibility
            checkoutsAbove100_1.visibility = visibility
            binding.lblCheckoutsPlus1001.visibility = visibility
            highestCO1.visibility = visibility
            binding.lblHighestCheckout1.visibility = visibility
            scoreAbove100_1.visibility = visibility
            binding.lblScoresPlus1001.visibility = visibility
            scoreAbove140_1.visibility = visibility
            binding.lblScoresPlus1401.visibility = visibility
            oneEighties1.visibility = visibility
            binding.lblOneEighties1.visibility = visibility
            player2.visibility = visibility
            avg2.visibility = visibility
            checkoutPct2.visibility = visibility
            checkouts2.visibility = visibility
            checkoutsAbove100_2.visibility = visibility
            highestCO2.visibility = visibility
            scoreAbove100_2.visibility = visibility
            scoreAbove140_2.visibility = visibility
            oneEighties2.visibility = visibility
        }

        companion object {
            fun from(parent: ViewGroup): DailySummaryViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RecyclermaintournamentitemBinding.inflate(layoutInflater, parent, false)
                return DailySummaryViewHolder(binding)
            }
        }
    }
}

class DailySummaryDiffCallback: DiffUtil.ItemCallback<DailySummary>(){
    override fun areItemsTheSame(oldItem: DailySummary, newItem: DailySummary): Boolean {
        return oldItem.sportEvent.id == newItem.sportEvent.id
    }

    override fun areContentsTheSame(oldItem: DailySummary, newItem: DailySummary): Boolean {
        return oldItem == newItem
    }
}
