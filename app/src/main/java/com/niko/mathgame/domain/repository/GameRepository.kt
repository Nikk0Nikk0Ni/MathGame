package com.niko.mathgame.domain.repository

import com.niko.mathgame.domain.entity.GameSettings
import com.niko.mathgame.domain.entity.Level
import com.niko.mathgame.domain.entity.Question

interface GameRepository {

    fun generateQuestion(
        maxSumValue : Int,
        countOfOptions : Int
    ) : Question

    fun getGameSettings(lvl : Level) : GameSettings
}