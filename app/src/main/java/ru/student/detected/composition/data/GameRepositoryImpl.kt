package ru.student.detected.composition.data

import ru.student.detected.composition.domain.entity.GameSettings
import ru.student.detected.composition.domain.entity.Level
import ru.student.detected.composition.domain.entity.Question
import ru.student.detected.composition.domain.repository.GameRepository

object GameRepositoryImpl: GameRepository {
    override fun generateQuestion(maxSumValue: Int, countOfOptions: Int): Question {
        val sum = (2..maxSumValue).random()
        val visibleNumber = (1 until sum).random()
        val ans = sum - visibleNumber
        val options = mutableSetOf(ans)

        while (options.size != countOfOptions){
            val num = (0..maxSumValue).random()
            options.add(num)
        }

        return Question(sum, visibleNumber, options.toList())
    }

    override fun getGameSettings(level: Level): GameSettings {
        return when (level){
            Level.TEST->GameSettings(
                10,
                3,
                50,
                8
            )
            Level.EASY->GameSettings(
                10,
                10,
                70,
                60
            )
            Level.NORMAL->GameSettings(
                20,
                20,
                80,
                40
            )
            Level.HARD->GameSettings(
                30,
                30,
                90,
                40
            )
        }
    }

}