package com.davygeeroms.dartsgames.fragments

import android.content.Context
import android.graphics.Color
import android.icu.text.DateTimePatternGenerator
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.marginLeft
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.viewpager.widget.ViewPager
import com.davygeeroms.dartsgames.R
import com.davygeeroms.dartsgames.databinding.FragmentWinnerBinding
import com.davygeeroms.dartsgames.persistence.AppDatabase
import com.davygeeroms.dartsgames.utilities.ColorInverter
import com.davygeeroms.dartsgames.viewmodels.ViewModelFactory
import com.davygeeroms.dartsgames.viewmodels.WinnerViewModel

class WinnerFragment : Fragment() {

    private lateinit var binding: FragmentWinnerBinding
    private lateinit var vm: WinnerViewModel
    private lateinit var vmFactory: ViewModelProvider.Factory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        //ActionBar titles
        (activity as AppCompatActivity).supportActionBar?.title = "DartsGames"
        (activity as AppCompatActivity).supportActionBar?.subtitle = "Winner, Winner, chicken dinner!"

        //args
        val args = PlayGameFragmentArgs.fromBundle(requireArguments())

        //application
        val application = requireNotNull(this.activity).application

        //binding
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_winner, container, false)

        //appDB
        val appDB = AppDatabase.getInstance(application)

        //ViewModel
        vmFactory = ViewModelFactory(application, appDB.gameDao)
        vm = ViewModelProviders.of(this, vmFactory).get(WinnerViewModel::class.java)
        vm.getFinalizedGame(args.gameId)


        vm.currentGame.observe(viewLifecycleOwner, Observer { game ->
            if(game != null){
                val winnerText = "Winner:\n${game.currentPlayer.name}!!"
                binding.lblWinningPlayer.text = winnerText
                val layoutParam = binding.lblWinningPlayer.layoutParams
                layoutParam.width = ViewPager.LayoutParams.MATCH_PARENT
                binding.lblWinningPlayer.layoutParams = layoutParam
                binding.lblWinningPlayer.setBackgroundColor(Color.parseColor(game.currentPlayer.color))
                binding.lblWinningPlayer.setTextColor(Color.parseColor(ColorInverter.ColorInverter.invertColor(game.currentPlayer.color)))

                val statTexts = game.getStats()

                for(stat in statTexts){
                    val tvStat = TextView(this.context, null)
                    tvStat.text = stat
                    tvStat.setTextColor(resources.getColor(R.color.text))
                    tvStat.textSize = 8.dpToPixels(requireContext()).toFloat()

                    binding.lnlPlayerStats.addView(tvStat)

                    (tvStat.layoutParams as LinearLayout.LayoutParams).apply {
                        // individually set text view any side margin
                        marginStart = 25.dpToPixels(requireContext())
                        topMargin = 10.dpToPixels(requireContext())
                        marginEnd = 25.dpToPixels(requireContext())
                        bottomMargin = 10.dpToPixels(requireContext())

                    }
                }
            }
        })

        return binding.root
    }

}

// extension function to convert dp to equivalent pixels
fun Int.dpToPixels(context: Context):Int = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP,this.toFloat(),context.resources.displayMetrics
).toInt()