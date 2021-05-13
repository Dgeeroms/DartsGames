package com.davygeeroms.dartsgames.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.davygeeroms.dartsgames.R
import com.davygeeroms.dartsgames.databinding.FragmentMainMenuBinding
import com.davygeeroms.dartsgames.viewmodels.MainMenuViewModel
import com.davygeeroms.dartsgames.viewmodels.ViewModelFactory


class MainMenuFragment : Fragment() {

    private lateinit var vm: MainMenuViewModel
    private lateinit var binding: FragmentMainMenuBinding

    lateinit var vmFactory: ViewModelProvider.Factory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //val args = MainFragmentArgs.fromBundle(requireArguments())

        //application
        val application = requireNotNull(this.activity).application

        //appDB
        //val appDB = AppDatabase.getInstance(application)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main_menu, container, false)

        //ViewModel
        vmFactory = ViewModelFactory(application =  application)
        vm = ViewModelProviders.of(this, vmFactory).get(MainMenuViewModel::class.java)

        //binding.mainMenuViewModel = vm
        binding.butContinueGame.isEnabled = false

        binding.butNewGame.setOnClickListener {
            view?.findNavController()?.navigate(MainMenuFragmentDirections.actionMainMenuFragmentToNewGameFragment())
        }

        return binding.root
    }

}