package com.niko.mathgame.presentation

import android.content.res.ColorStateList
import android.os.Build
import android.os.Build.VERSION
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.niko.mathgame.R
import com.niko.mathgame.databinding.FragmentGameBinding
import com.niko.mathgame.domain.entity.GameResult
import com.niko.mathgame.domain.entity.GameSettings
import com.niko.mathgame.domain.entity.Level
import com.niko.mathgame.presentation.ViewModels.GameViewModel

class GameFragment : Fragment() {

    private val viewModel: GameViewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(requireActivity().application)
        )[GameViewModel::class.java]
    }
    private var _binding: FragmentGameBinding? = null
    private lateinit var lvl: Level
    private val binding: FragmentGameBinding
        get() = _binding ?: throw RuntimeException("Fragment Game Binding == null")
    private val listOptions by lazy {
        mutableListOf<TextView>().apply {
            add(binding.tvAnsw1)
            add(binding.tvAnsw2)
            add(binding.tvAnsw3)
            add(binding.tvAnsw4)
            add(binding.tvAnsw5)
            add(binding.tvAnsw6)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArgs()
        viewModel.startGame(lvl)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun observeViewModel(){
        initTimer()
        setQuestion()
        initOptionBtns()
        initTvProgress()
        initProgressBar()
        initFinishGame()
    }

    private fun initFinishGame() {
        viewModel.gameResult.observe(viewLifecycleOwner){
            launchGameFinishFragment(it)
        }
    }

    private fun initProgressBar() {
        viewModel.minPercent.observe(viewLifecycleOwner){
            binding.progressBar.secondaryProgress = it
        }
        viewModel.percentOfRignhtAnswers.observe(viewLifecycleOwner){
            binding.progressBar.setProgress(it,true)
        }
        viewModel.enoughPercentOfRightAnswers.observe(viewLifecycleOwner){
            binding.progressBar.progressTintList = ColorStateList.valueOf(getColorByState(it))
        }
    }

    private fun initOptionBtns() = with(binding) {
        for(i in listOptions.indices){
            listOptions[i].setOnClickListener{
                viewModel.chooseAnswer(listOptions[i].text.toString().toInt())
            }
        }
    }

    private fun getColorByState(state : Boolean) : Int{
        return if(state) ContextCompat.getColor(requireContext(),android.R.color.holo_green_light) else ContextCompat.getColor(requireContext(),android.R.color.holo_red_light)
    }

    private fun initTvProgress() {
        viewModel.progressAnswers.observe(viewLifecycleOwner) {
            binding.tvAnswerProgress.text = it
        }
        viewModel.enoughCountOfRightAnswers.observe(viewLifecycleOwner){
            binding.tvAnswerProgress.setTextColor(getColorByState(it))
        }
    }

    private fun setQuestion() {
        viewModel.qustion.observe(viewLifecycleOwner) {
            binding.apply {
                tvResult.text = it.sum.toString()
                tvVisibleNumber.text = it.visibleNumber.toString()
                for(i in listOptions.indices){
                    listOptions[i].text = it.options[i].toString()
                }
            }
        }
    }

    private fun initTimer() {
        viewModel.time.observe(viewLifecycleOwner) {
            binding.tvTimer.text = it
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
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

    private fun parseArgs() {
        if (VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            requireArguments().getParcelable(KEY_LVL, Level::class.java)?.let {
                lvl = it
            } else {
            requireArguments().getParcelable<Level>(KEY_LVL)?.let {
                lvl = it
            }
        }
    }

    companion object {
        private const val KEY_LVL = "level"
        const val NAME = "name"
        fun newInstance(lvl: Level): GameFragment {
            return GameFragment().apply {
                this.arguments = Bundle().apply {
                    this.putParcelable(KEY_LVL, lvl)
                }
            }

        }
    }

}