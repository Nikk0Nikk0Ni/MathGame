package com.niko.mathgame.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.niko.mathgame.R
import com.niko.mathgame.databinding.FragmentGameBinding
import com.niko.mathgame.domain.entity.GameResult
import com.niko.mathgame.domain.entity.GameSettings
import com.niko.mathgame.domain.entity.Level

class GameFragment : Fragment() {
    private var _binding : FragmentGameBinding? = null
    private lateinit var lvl : Level
    private val binding : FragmentGameBinding
        get() = _binding ?: throw RuntimeException("Fragment Game Binding == null")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArgs()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvResult.setOnClickListener{
            launchGameFinishFragment(
                GameResult(true, 1, 1,
                    GameSettings(1, 1, 1, 1)))
        }
    }

    private fun launchGameFinishFragment(gameResult: GameResult) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(
                R.id.main_container, ResultFragment
                    .newInstance(gameResult)
            )
            .addToBackStack(null).commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun parseArgs(){
        lvl = requireArguments().getSerializable(KEY_LVL) as Level
    }

    companion object{
        private const val KEY_LVL = "level"
        const val NAME = "name"
        fun newInstance(lvl : Level) : GameFragment{
            return GameFragment().apply {
                this.arguments = Bundle().apply {
                    this.putSerializable(KEY_LVL,lvl)
                }
            }

        }
    }

}