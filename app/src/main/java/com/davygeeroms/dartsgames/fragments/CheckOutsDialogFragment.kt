package com.davygeeroms.dartsgames.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.davygeeroms.dartsgames.R
import com.davygeeroms.dartsgames.databinding.CheckoutsDialogBinding
import com.davygeeroms.dartsgames.enums.CheckOutTable

class CheckOutsDialogFragment(val checkOut: CheckOutTable): DialogFragment()  {

    private lateinit var binding: CheckoutsDialogBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        //binding
        binding = DataBindingUtil.inflate(inflater, R.layout.checkouts_dialog, container, false)

        //round corners on dialog box
        val drawable = this.context?.let { AppCompatResources.getDrawable(it, R.drawable.round_corners_red_border) }
        dialog!!.window?.setBackgroundDrawable(drawable)

        val checkOutsStr = checkOut.combination.split(";")
        var text = ""

        for(cot in checkOutsStr){
            text += cot + "\n"
        }

        binding.lblPossibleCheckoutsValues.text = text

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        val height = (resources.displayMetrics.heightPixels * 0.40).toInt()
        dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }


}