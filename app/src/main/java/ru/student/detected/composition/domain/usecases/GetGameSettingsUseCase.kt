package ru.student.detected.composition.domain.usecases

import ru.student.detected.composition.domain.repository.GameRepository
import ru.student.detected.composition.domain.entity.GameSettings
import ru.student.detected.composition.domain.entity.Level

class GetGameSettingsUseCase(
    private val repository: GameRepository
) {

    operator fun invoke(level: Level): GameSettings {
        return repository.getGameSettings(level)
    }
}