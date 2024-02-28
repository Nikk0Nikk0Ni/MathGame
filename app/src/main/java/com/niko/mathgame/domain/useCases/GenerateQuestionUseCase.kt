package com.niko.mathgame.domain.useCases

import com.niko.mathgame.domain.entity.GameSettings
import com.niko.mathgame.domain.entity.Question
import com.niko.mathgame.domain.repository.GameRepository

class GenerateQuestionUseCase(
    private val repository: GameRepository
) {
    operator fun invoke(maxSumValue: Int): Question {
        return repository.generateQuestion(maxSumValue, COUNT_OF_OPTIONS)
    }

    private companion object {
        private const val COUNT_OF_OPTIONS = 6
    }
}