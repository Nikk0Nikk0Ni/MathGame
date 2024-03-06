package com.niko.mathgame.presentation.ViewModels

import android.app.Application
import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.niko.mathgame.R
import com.niko.mathgame.data.Repository.GameRepositoryImpl
import com.niko.mathgame.domain.entity.GameResult
import com.niko.mathgame.domain.entity.GameSettings
import com.niko.mathgame.domain.entity.Level
import com.niko.mathgame.domain.entity.Question
import com.niko.mathgame.domain.useCases.GenerateQuestionUseCase
import com.niko.mathgame.domain.useCases.GetGameSettingsUseCase

class GameViewModel(private val application: Application,private val level : Level) : ViewModel() {

    private lateinit var gameSettings: GameSettings

    private var timer: CountDownTimer? = null

    private val _qustion = MutableLiveData<Question>()
    val qustion: LiveData<Question>
        get() = _qustion

    private var countOfRightAnswers = 0
    private var countOfQuestions = 0

    private val _percentOfRightAnswers = MutableLiveData<Int>()
    val percentOfRignhtAnswers: LiveData<Int>
        get() = _percentOfRightAnswers

    private val _progressAnswers = MutableLiveData<String>()
    val progressAnswers: LiveData<String>
        get() = _progressAnswers

    private val _enoughCountOfRightAnswers = MutableLiveData<Boolean>()
    val enoughCountOfRightAnswers: LiveData<Boolean>
        get() = _enoughCountOfRightAnswers

    private val _enoughPercentOfRightAnswers = MutableLiveData<Boolean>()
    val enoughPercentOfRightAnswers: LiveData<Boolean>
        get() = _enoughPercentOfRightAnswers

    private val _minPercent = MutableLiveData<Int>()
    val minPercent: LiveData<Int>
        get() = _minPercent

    private val _gameResult = MutableLiveData<GameResult>()
    val gameResult: LiveData<GameResult>
        get() = _gameResult

    private val repository = GameRepositoryImpl
    private val getQuestion = GenerateQuestionUseCase(repository)
    private val getSettings = GetGameSettingsUseCase(repository)

    private val _time = MutableLiveData<String>()
    val time: LiveData<String>
        get() = _time

    init {
        startGame()
    }

    fun startGame() {
        getGameSettings()
        startTimer()
        generateQuestion()
        updateProgress()
    }

    fun updateProgress() {
        val percent = calcPercentProgress()
        _percentOfRightAnswers.value = percent
        _progressAnswers.value = String.format(
            application.resources.getString(R.string.correct_answers_s_minimum_s),
            countOfRightAnswers,
            gameSettings.minCountOfRightAnswers
        )
        _enoughCountOfRightAnswers.value =
            countOfRightAnswers >= gameSettings.minCountOfRightAnswers
        _enoughPercentOfRightAnswers.value = percent >= gameSettings.minPercentOfRightAnswers
    }

    private fun calcPercentProgress(): Int {
        if (countOfQuestions == 0)
            return 0
        else
            return ((countOfRightAnswers / countOfQuestions.toDouble()) * 100).toInt()
    }

    private fun generateQuestion() {
        _qustion.value = getQuestion(gameSettings.maxSumValue)
    }

    fun chooseAnswer(answer: Int) {
        checkAnswer(answer)
        updateProgress()
        generateQuestion()
    }

    private fun checkAnswer(answer: Int) {
        if (qustion.value?.rightAnswer == answer)
            countOfRightAnswers++
        countOfQuestions++
    }

    private fun getGameSettings() {
        gameSettings = getSettings(level)
        _minPercent.value = gameSettings.minPercentOfRightAnswers
    }

    private fun startTimer() {
        timer = object : CountDownTimer(
            gameSettings.gameTimeInSeconds * MILLIS_IN_SECONDS, MILLIS_IN_SECONDS
        ) {
            override fun onTick(p0: Long) {
                _time.value = formateTime(p0)
            }

            override fun onFinish() {
                finishGame()
            }

        }
        timer?.start()
    }


    private fun formateTime(amountOfMillis: Long): String {
        val second = amountOfMillis / MILLIS_IN_SECONDS
        val minutes = second / SECONDS_IN_MINUTES
        val leftSec = second - (minutes * SECONDS_IN_MINUTES)
        return String.format("%02d:%02d", minutes, leftSec)
    }

    private fun finishGame() {
        _gameResult.value = GameResult(
            winner = enoughCountOfRightAnswers.value == true &&
                    enoughPercentOfRightAnswers.value == true,
            countOfQuestions = countOfQuestions,
            countOfRightAnswers = countOfRightAnswers,
            gameSettings = gameSettings
        )
    }

    override fun onCleared() {
        super.onCleared()
        timer?.cancel()

    }

    companion object {
        const val MILLIS_IN_SECONDS = 1000L
        const val SECONDS_IN_MINUTES = 60
    }
}