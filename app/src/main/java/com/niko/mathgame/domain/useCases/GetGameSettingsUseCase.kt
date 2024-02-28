package com.niko.mathgame.domain.useCases

import com.niko.mathgame.domain.entity.GameSettings
import com.niko.mathgame.domain.entity.Level
import com.niko.mathgame.domain.repository.GameRepository

class GetGameSettingsUseCase(
    private val repository: GameRepository
) {
    operator fun invoke(lvl : Level) : GameSettings{
        return repository.getGameSettings(lvl)
    }
}