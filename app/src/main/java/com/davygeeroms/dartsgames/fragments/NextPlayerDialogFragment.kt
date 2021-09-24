package com.davygeeroms.dartsgames.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.davygeeroms.dartsgames.R
import com.davygeeroms.dartsgames.databinding.NextPlayerDialogBinding


class NextPlayerDialogFragment(val playerName: String): DialogFragment() {

    private lateinit var binding: NextPlayerDialogBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        //binding
        binding = DataBindingUtil.inflate(inflater, R.layout.next_player_dialog, container, false)
        //round corners on dialog box
        dialog!!.window?.setBackgroundDrawableResource(R.drawable.round_corners)

        val text = "Next player: $playerName"
        binding.lblNextPlayer.text = text

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        val height = (resources.displayMetrics.heightPixels * 0.40).toInt()
        dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

}