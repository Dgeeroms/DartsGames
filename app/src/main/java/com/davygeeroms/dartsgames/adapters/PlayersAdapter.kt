package com.davygeeroms.dartsgames.adapters

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.RecyclerView
import com.davygeeroms.dartsgames.R
import com.davygeeroms.dartsgames.entities.Player
import com.davygeeroms.dartsgames.inflate
import kotlinx.android.synthetic.main.recyclerplayersitem.view.*

class PlayersAdapter(private val dataSet: ArrayList<Player>) :
    RecyclerView.Adapter<PlayersAdapter.PlayerHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.textView)

        init {

        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): PlayerHolder {
        // Create a new view, which defines the UI of the list item
        val inflatedView = viewGroup.inflate(R.layout.recyclerplayersitem, false)
        return PlayerHolder(inflatedView)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(playerHolder: PlayerHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        val player = dataSet[position]
        playerHolder.bindPlayer(player)


    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

    class PlayerHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {

        private var view: View = v
        private var player: Player? = null


        init {
            v.setOnClickListener(this)
        }


        override fun onClick(v: View) {
            //Log.d("RecyclerView", "CLICK!")
        }

        companion object {
            private val PLAYER_KEY = "PLAYER"
        }

        fun bindPlayer(player: Player) {
            this.player = player

            view.textView.text = player.name
            view.setBackgroundColor(player.color.toColorInt())
        }

    }
}


