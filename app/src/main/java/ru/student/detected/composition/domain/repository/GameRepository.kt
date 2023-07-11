package ru.student.detected.composition.domain.repository

import ru.student.detected.composition.domain.entity.GameSettings
import ru.student.detected.composition.domain.entity.Level
import ru.student.detected.composition.domain.entity.Question

interface GameRepository {

    fun generateQuestion(
        maxSumValue: Int,
        countOfOptions: Int
    ): Question

    fun getGameSettings(level: Level): GameSettings
}