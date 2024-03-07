package com.niko.mathgame.presentation

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.niko.mathgame.R
import com.niko.mathgame.databinding.FragmentLevelBinding
import com.niko.mathgame.domain.entity.Level

class LevelFragment : Fragment() {

    private var _binding: FragmentLevelBinding? = null
    private val binding: FragmentLevelBinding
        get() = _binding ?: throw RuntimeException("Fragment Level Binding == null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLevelBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBtns()
    }

    private fun initBtns() = with(binding) {
        btnTestLvl.setOnClickListener {
            launchGameFragment(Level.TEST)
        }
        btnSimpleLvl.setOnClickListener {
            launchGameFragment(Level.EASY)
        }
        btnMediumLvl.setOnClickListener {
            launchGameFragment(Level.NORMAL)
        }
        btnHardLvl.setOnClickListener {
            launchGameFragment(Level.HARD)
        }
    }

    private fun launchGameFragment(lvl : Level) {
        findNavController().navigate(LevelFragmentDirections.actionLevelFragmentToGameFragment(lvl))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}