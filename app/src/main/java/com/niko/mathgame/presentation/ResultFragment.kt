package com.niko.mathgame.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.FragmentManager
import com.niko.mathgame.R
import com.niko.mathgame.databinding.FragmentResultBinding
import com.niko.mathgame.databinding.FragmentWelcomeBinding
import com.niko.mathgame.domain.entity.GameResult
import com.niko.mathgame.domain.entity.GameSettings

class ResultFragment : Fragment() {
    private var _binding: FragmentResultBinding? = null
    private lateinit var gameResult: GameResult
    private val binding: FragmentResultBinding
        get() = _binding ?: throw RuntimeException("Fragment Welcome Binding == null")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArgs()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBackBtns()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun parseArgs() {
        gameResult = requireArguments().getSerializable(GAME_RESULT) as GameResult
    }

    private fun initBackBtns() {
        binding.btnRetry.setOnClickListener {
            backToMenu()
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    backToMenu()
                }

            })
    }

    private fun backToMenu() {
        requireActivity().supportFragmentManager.popBackStack(
            GameFragment.NAME,
            FragmentManager.POP_BACK_STACK_INCLUSIVE
        )
    }

    companion object {
        private const val GAME_RESULT = "game_result"
        fun newInstance(gameResult: GameResult): ResultFragment {
            return ResultFragment().apply {
                this.arguments = Bundle().apply {
                    putSerializable(GAME_RESULT, gameResult)
                }
            }
        }
    }
}