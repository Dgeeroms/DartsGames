package com.davygeeroms.dartsgames.fragments

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.toColorInt
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.davygeeroms.dartsgames.R
import com.davygeeroms.dartsgames.databinding.NextPlayerDialogBinding
import com.davygeeroms.dartsgames.entities.Player
import com.davygeeroms.dartsgames.utilities.ColorInverter


class NextPlayerDialogFragment(val player: Player): DialogFragment() {

    private lateinit var binding: NextPlayerDialogBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        //binding
        binding = DataBindingUtil.inflate(inflater, R.layout.next_player_dialog, container, false)

        //round corners on dialog box + colors
        val drawable = this.context?.let { AppCompatResources.getDrawable(it, R.drawable.round_corners) }
        drawable?.setTint(Color.parseColor(player.color))
        dialog!!.window?.setBackgroundDrawable(drawable)


        val text = "Next player: ${player.name}"
        binding.lblNextPlayer.text = text
        binding.nxtPlayerDialog.setBackgroundColor(Color.parseColor(player.color))
        binding.lblNextPlayer.setTextColor(Color.parseColor(ColorInverter.ColorInverter.invertColor(player.color)))

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        val height = (resources.displayMetrics.heightPixels * 0.40).toInt()
        dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

}