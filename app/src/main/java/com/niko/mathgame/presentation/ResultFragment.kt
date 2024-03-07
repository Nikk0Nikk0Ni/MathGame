package com.niko.mathgame.presentation

import android.os.Build
import android.os.Build.VERSION
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.niko.mathgame.R
import com.niko.mathgame.databinding.FragmentResultBinding
import com.niko.mathgame.domain.entity.GameResult

class ResultFragment : Fragment() {
    private var _binding: FragmentResultBinding? = null
    private val gameResult: GameResult by lazy {
        args.result
    }

    private val args : ResultFragmentArgs by navArgs()
    private val binding: FragmentResultBinding
        get() = _binding ?: throw RuntimeException("Fragment Welcome Binding == null")


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun chooseLaucnhSettings(isWinner: Boolean) {
        if (isWinner) {
            binding.imgEmoji.setImageResource(R.drawable.smile)
            setTextResult()
        } else {
            binding.imgEmoji.setImageResource(R.drawable.sad)
            setTextResult()
        }
    }

    private fun setTextResult() {
        val percent =  if (gameResult.countOfQuestions == 0) 0 else (gameResult.countOfRightAnswers/gameResult.countOfQuestions.toDouble())*100
        binding.tvReqAmountRightAnswer.text = String.format(
            resources.getString(R.string.required_score),
            gameResult.gameSettings.minCountOfRightAnswers.toString()
        )
        binding.tvReqPercRightAnswer.text = String.format(
            resources.getString(R.string.required_percentage),
            gameResult.gameSettings.minPercentOfRightAnswers.toString()
        )
        binding.tvUrAmountRightAnswer.text = String.format(
            resources.getString(R.string.your_score),
            gameResult.countOfRightAnswers.toString()
        )
        binding.tvUrPercRightAnswer.text = String.format(
            resources.getString(R.string.your_percentage),
            percent.toInt()
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        chooseLaucnhSettings(gameResult.winner)
        initBackBtns()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initBackBtns() {
        binding.btnRetry.setOnClickListener {
            backToMenu()
        }
    }

    private fun backToMenu() {
        findNavController().popBackStack()
    }

}