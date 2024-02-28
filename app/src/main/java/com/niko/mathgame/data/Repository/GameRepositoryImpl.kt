package com.niko.mathgame.data.Repository

import com.niko.mathgame.domain.entity.GameSettings
import com.niko.mathgame.domain.entity.Level
import com.niko.mathgame.domain.entity.Question
import com.niko.mathgame.domain.repository.GameRepository
import kotlin.math.max
import kotlin.random.Random

object GameRepositoryImpl : GameRepository {
    private const val MIN_SUM_VALUE = 2
    private const val MIN_ANSWER_VALUE = 1
    override fun generateQuestion(maxSumValue: Int, countOfOptions: Int): Question {
        val sum = Random.nextInt(MIN_SUM_VALUE, maxSumValue + 1)
        val visibleNumber = Random.nextInt(MIN_ANSWER_VALUE, sum)
        val rightAnswer = sum - visibleNumber
        val listOfAnswer = hashSetOf(rightAnswer)
        while (listOfAnswer.size < countOfOptions)
            listOfAnswer.add(Random.nextInt(0, maxSumValue))
        return Question(sum, visibleNumber, listOfAnswer.toList())
    }

    override fun getGameSettings(lvl: Level): GameSettings {
        return when (lvl) {
            Level.TEST -> GameSettings(10, 3, 50, 10)
            Level.EASY -> GameSettings(100, 10, 50, 60)
            Level.NORMAL -> GameSettings(1000, 25, 70, 120)
            Level.HARD -> GameSettings(10000, 50, 90, 300)
        }
    }
}