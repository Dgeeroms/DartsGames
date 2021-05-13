package com.davygeeroms.dartsgames.fragments

import com.davygeeroms.dartsgames.adapters.PlayersAdapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.davygeeroms.dartsgames.R
import com.davygeeroms.dartsgames.databinding.FragmentNewGameBinding
import com.davygeeroms.dartsgames.entities.Player
import com.davygeeroms.dartsgames.enums.GameModes
import com.davygeeroms.dartsgames.viewmodels.NewGameViewModel
import kotlinx.android.synthetic.main.fragment_new_game.*
import kotlinx.android.synthetic.main.fragment_new_game.view.*

class NewGameFragment : Fragment() {

    private val vm: NewGameViewModel by activityViewModels()
    private lateinit var binding: FragmentNewGameBinding
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var playersAdapter: PlayersAdapter
    private lateinit var newPlayerRecyclerView: RecyclerView

    lateinit var vmFactory: ViewModelProvider.Factory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //application
        val application = requireNotNull(this.activity).application
        //binding
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_new_game, container, false)

        //spinner
        var gameModes:MutableList<String> = mutableListOf<String>()
        enumValues<GameModes>().forEach { gameModes.add(it.mode) }
        val realmSpinner = binding.spinnerGameTypes
        var spinnerAdapter = this.context?.let { ArrayAdapter(it, R.layout.spinneritem,  gameModes) }
        if (spinnerAdapter != null) {
            spinnerAdapter.setDropDownViewResource(R.layout.spinnerdropdown)
            realmSpinner.adapter = spinnerAdapter
        }

        //recycler
        newPlayerRecyclerView = binding.recyclerView
        linearLayoutManager = LinearLayoutManager(this.context)
        newPlayerRecyclerView.layoutManager = linearLayoutManager
        playersAdapter = PlayersAdapter(vm.players.value as ArrayList<Player>)
        newPlayerRecyclerView.adapter = playersAdapter

        //addPlayerbutton
        binding.butAddPlayer.setOnClickListener {
            insertPlayer(it);
        }
        return binding.root
    }

    private fun insertPlayer(view: View){

        val player = Player(playersAdapter.itemCount - 1, view.editTextPlayerName.text.toString())
        vm.addPlayer(player)

        playersAdapter.notifyItemInserted(player.number)

    }

    fun removePlayer(view:View){

    }
}