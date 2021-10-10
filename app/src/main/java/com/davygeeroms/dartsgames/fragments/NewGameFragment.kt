package com.davygeeroms.dartsgames.fragments

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.SeekBar
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.DrawableCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.davygeeroms.dartsgames.R
import com.davygeeroms.dartsgames.adapters.PlayersAdapter
import com.davygeeroms.dartsgames.databinding.FragmentNewGameBinding
import com.davygeeroms.dartsgames.entities.Player
import com.davygeeroms.dartsgames.enums.GameModes
import com.davygeeroms.dartsgames.persistence.AppDatabase
import com.davygeeroms.dartsgames.utilities.ColorInverter
import com.davygeeroms.dartsgames.viewmodels.NewGameViewModel
import com.davygeeroms.dartsgames.viewmodels.ViewModelFactory
import kotlinx.android.synthetic.main.colorpicker.view.*
import java.util.*

class NewGameFragment : Fragment() {

    private lateinit var vm: NewGameViewModel
    private lateinit var vmFactory: ViewModelProvider.Factory
    private lateinit var binding: FragmentNewGameBinding
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var playersAdapter: PlayersAdapter
    private lateinit var newPlayerRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        //application
        val application = requireNotNull(this.activity).application
        //binding
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_new_game, container, false)

        //appDB
        val appDB = AppDatabase.getInstance(application)
        //ViewModel
        vmFactory = ViewModelFactory(application, appDB.gameDao)
        vm = ViewModelProviders.of(this, vmFactory).get(NewGameViewModel::class.java)

        //spinner
        val gameModes:MutableList<String> = mutableListOf()
        enumValues<GameModes>().forEach { gameModes.add(it.mode) }
        val realmSpinner = binding.spinnerGameTypes
        val spinnerAdapter = this.context?.let { ArrayAdapter(it, R.layout.spinneritem,  gameModes) }
        if (spinnerAdapter != null) {
            spinnerAdapter.setDropDownViewResource(R.layout.spinnerdropdown)
            realmSpinner.adapter = spinnerAdapter
        }

        //recycler
        newPlayerRecyclerView = binding.recyclerView
        linearLayoutManager = LinearLayoutManager(this.context)
        newPlayerRecyclerView.layoutManager = linearLayoutManager
        playersAdapter = PlayersAdapter(vm.players.value as ArrayList<Player>){player ->
            if (player != null) {
                removePlayer(player)
            }
        }
        newPlayerRecyclerView.adapter = playersAdapter

        //addPlayerbutton
        binding.butAddPlayer.setOnClickListener {
            if(binding.editTextPlayerName.text.toString().isNotBlank()) {
                insertPlayer()
                binding.editTextPlayerName.setText("")
                it.hideKeyboard()
            }
        }

        //set btn off screen
        binding.butAddPlayer.visibility = View.INVISIBLE
        binding.butAddPlayer.animate().translationX(-1000F)
        binding.editTextPlayerName.addTextChangedListener(object: TextWatcher{

            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(binding.editTextPlayerName.text.toString().isNotBlank()){
                    binding.butAddPlayer.visibility = View.VISIBLE
                    binding.butAddPlayer.animate().translationX(-0F)
                } else {
                    binding.butAddPlayer.animate().translationX(-1000F)
                    //binding.butAddPlayer.visibility = View.INVISIBLE
                }
            }

        })

        //colorpicker
        initColorPicker()

        //play game button
        binding.playbutton.animate().translationX(1000F)
        vm.players.observe(viewLifecycleOwner, Observer { players ->
            if(players.count() != 0){
                binding.playbutton.visibility = View.VISIBLE
                binding.playbutton.animate().translationX(0F)
            } else {
                binding.playbutton.animate().translationX(1000F)
                //binding.playbutton.visibility = View.INVISIBLE
            }
        })


        binding.playbutton.setOnClickListener {

            val selectedGameMode = GameModes.values().first { gm -> gm.mode == binding.spinnerGameTypes.selectedItem as String }
            vm.startGame(selectedGameMode)

        }

        vm.game.observe(viewLifecycleOwner, Observer { game ->
            if(game != null){
                view?.findNavController()?.navigate(NewGameFragmentDirections.actionNewGameFragmentToPlayGameFragment(game.id))
            }
        })

        return binding.root
    }



    private fun View.hideKeyboard() {
        val inputManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(windowToken, 0)
    }

    private fun initColorPicker(){


        binding.btnColorPicker.setOnClickListener {
            binding.colorPicker.visibility = View.VISIBLE
            it.hideKeyboard()
        }

        binding.colorPicker.strColor.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                if (s.length == 6){
                    //binding.colorPicker.colorA.progress = 255
                    binding.colorPicker.colorR.progress = Integer.parseInt(s.substring(0..1), 16)
                    binding.colorPicker.colorG.progress = Integer.parseInt(s.substring(2..3), 16)
                    binding.colorPicker.colorB.progress = Integer.parseInt(s.substring(4..5), 16)
                } else if (s.length == 8){
                    //binding.colorPicker.colorA.progress = Integer.parseInt(s.substring(0..1), 16)
                    binding.colorPicker.colorR.progress = Integer.parseInt(s.substring(2..3), 16)
                    binding.colorPicker.colorG.progress = Integer.parseInt(s.substring(4..5), 16)
                    binding.colorPicker.colorB.progress = Integer.parseInt(s.substring(6..7), 16)
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {

            }
        })
/*
        binding.colorPicker.colorA.max = 255
        binding.colorPicker.colorA.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onProgressChanged(seekBar: SeekBar, progress: Int,
                                           fromUser: Boolean) {
                val colorStr = getColorString()
                binding.colorPicker.strColor.setText(colorStr.replace("#","").toUpperCase())
                binding.colorPicker.btnColorPreview.setBackgroundColor(Color.parseColor(colorStr))
            }
        })*/

        binding.colorPicker.colorR.max = 255
        binding.colorPicker.colorR.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onProgressChanged(seekBar: SeekBar, progress: Int,
                                           fromUser: Boolean) {
                val colorStr = getColorString()
                binding.colorPicker.strColor.setText(colorStr.replace("#","").toUpperCase(Locale.getDefault()))
                binding.colorPicker.btnColorPreview.setBackgroundColor(Color.parseColor(colorStr))
            }
        })

        binding.colorPicker.colorG.max = 255
        binding.colorPicker.colorG.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onProgressChanged(seekBar: SeekBar, progress: Int,
                                           fromUser: Boolean) {
                val colorStr = getColorString()
                binding.colorPicker.strColor.setText(colorStr.replace("#","").toUpperCase(Locale.getDefault()))
                binding.colorPicker.btnColorPreview.setBackgroundColor(Color.parseColor(colorStr))
            }
        })

        binding.colorPicker.colorB.max = 255
        binding.colorPicker.colorB.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onProgressChanged(seekBar: SeekBar, progress: Int,
                                           fromUser: Boolean) {
                val colorStr = getColorString()
                binding.colorPicker.strColor.setText(colorStr.replace("#","").toUpperCase(Locale.getDefault()))
                binding.colorPicker.btnColorPreview.setBackgroundColor(Color.parseColor(colorStr))
            }
        })

        binding.colorPicker.colorCancelBtn.setOnClickListener {
            binding.colorPicker.visibility = View.GONE
        }

        binding.colorPicker.colorOkBtn.setOnClickListener {
            val color:String = getColorString()

            binding.btnColorPicker.setBackgroundColor(Color.parseColor(color))
            binding.btnColorPicker.setTextColor(Color.parseColor(ColorInverter.ColorInverter.invertColor(color)))
            binding.colorPicker.visibility = View.GONE
        }
    }
    fun getColorString(): String {
        var a = Integer.toHexString(255)
        if(a.length==1) a = "0$a"
        var r = Integer.toHexString(((255*binding.colorPicker.colorR.progress)/binding.colorPicker.colorR.max))
        if(r.length==1) r = "0$r"
        var g = Integer.toHexString(((255*binding.colorPicker.colorG.progress)/binding.colorPicker.colorG.max))
        if(g.length==1) g = "0$g"
        var b = Integer.toHexString(((255*binding.colorPicker.colorB.progress)/binding.colorPicker.colorB.max))
        if(b.length==1) b = "0$b"
        return "#$a$r$g$b"
    }


    private fun insertPlayer(){

        val player = Player(playersAdapter.itemCount + 1, binding.editTextPlayerName.text.toString(), getColorString())
        vm.addPlayer(player)

        playersAdapter.notifyItemInserted(player.number - 1)

    }

    private fun removePlayer(player: Player){

        vm.removePlayer(player)
        playersAdapter.notifyItemRemoved(player.number - 1)

    }
}