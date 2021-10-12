package com.davygeeroms.dartsgames.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.davygeeroms.dartsgames.R
import com.davygeeroms.dartsgames.entities.Turn
import com.davygeeroms.dartsgames.utilities.ColorInverter
import kotlinx.android.synthetic.main.recyclerplayerhistoryitem.view.*
import kotlinx.android.synthetic.main.recyclerplayersitem.view.*

class PlayerHistoryAdapter :
    RecyclerView.Adapter<PlayerHistoryAdapter.PlayerHistoryHolder>() {

    var dataSet = listOf<Turn>()
            set(value){
                field = value
                notifyDataSetChanged()
            }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): PlayerHistoryHolder {

        // Create a new view, which defines the UI of the list item
        val inflater = LayoutInflater.from(viewGroup.context)
        return PlayerHistoryHolder(inflater.inflate(R.layout.recyclerplayerhistoryitem, viewGroup, false))
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(playerHistoryHolder: PlayerHistoryHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        val playerHistory = dataSet[position]
        playerHistoryHolder.bindPlayerScoreHistory(playerHistory)
    }


    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size


    class PlayerHistoryHolder(private var view: View) : RecyclerView.ViewHolder(view) {

        private var playerHistory: Turn? = null


        fun bindPlayerScoreHistory(playerHist: Turn) {
            this.playerHistory = playerHist

            //texts
            view.textView_player_name.text = playerHist.playerScore.player.name
            view.textView_dart.text = playerHist.darts.lastOrNull()?.description ?: ""
            view.textView_score.text = playerHist.playerScore.score.toString()

            //colors
            view.setBackgroundColor(playerHist.playerScore.player.color.toColorInt())
            view.textView_player_name.setTextColor(Color.parseColor(ColorInverter.ColorInverter.invertColor(playerHist.playerScore.player.color)))
            view.textView_dart.setTextColor(Color.parseColor(ColorInverter.ColorInverter.invertColor(playerHist.playerScore.player.color)))
            view.textView_score.setTextColor(Color.parseColor(ColorInverter.ColorInverter.invertColor(playerHist.playerScore.player.color)))


            //round corners on recycler item
            val drawable = view.context.let { AppCompatResources.getDrawable(it, R.drawable.round_corners) }
            drawable?.setTint(Color.parseColor(playerHist.playerScore.player.color))
            view.setBackgroundDrawable(drawable)
        }
    }
}
/*
class PlayerHistoryDiffCallback : DiffUtil.ItemCallback<PlayerScoreHistory>(){
    override fun areItemsTheSame(oldItem: PlayerScoreHistory, newItem: PlayerScoreHistory): Boolean {
        return oldItem.timestamp == newItem.timestamp
    }

    override fun areContentsTheSame(oldItem: PlayerScoreHistory, newItem: PlayerScoreHistory): Boolean {
        return oldItem == newItem
    }


}
 */


