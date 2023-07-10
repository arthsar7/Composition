package ru.student.detected.composition.presentation

import android.app.Application
import android.os.CountDownTimer
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.student.detected.composition.R
import ru.student.detected.composition.data.GameRepositoryImpl
import ru.student.detected.composition.domain.entity.GameResult
import ru.student.detected.composition.domain.entity.GameSettings
import ru.student.detected.composition.domain.entity.Level
import ru.student.detected.composition.domain.entity.Question
import ru.student.detected.composition.domain.usecases.GenerateQuestionUseCase
import ru.student.detected.composition.domain.usecases.GetGameSettingsUseCase


class GameViewModel(application: Application) : AndroidViewModel(application) {
    private var timer: CountDownTimer? = null
    private val repository = GameRepositoryImpl
    private val generateQuestionUseCase = GenerateQuestionUseCase(repository)
    private val getGameSettingsUseCase = GetGameSettingsUseCase(repository)
    private val context = application

    private val _enoughRightAnswers = MutableLiveData<Boolean>()
    val enoughRightAnswers: LiveData<Boolean>
        get() = _enoughRightAnswers

    private val _enoughPercentRightAnswers = MutableLiveData<Boolean>()
    val enoughPercentRightAnswers: LiveData<Boolean>
        get() = _enoughPercentRightAnswers

    private val _formattedTime = MutableLiveData<String>()
    val formattedTime: LiveData<String>
        get() = _formattedTime

    private val _question = MutableLiveData<Question>()
    val question: LiveData<Question>
        get() = _question

    private val _percentOfRightAnswers = MutableLiveData<Int>()
    val percentOfRightAnswers: LiveData<Int>
        get() = _percentOfRightAnswers

    private val _progressAnswers = MutableLiveData<String>()
    val progressAnswers: LiveData<String>
        get() = _progressAnswers

    private val _minPercent = MutableLiveData<Int>()
    val minPercent: LiveData<Int>
        get() = _minPercent

    private val _gameResult = MutableLiveData<GameResult>()
    val gameResult: LiveData<GameResult>
        get() = _gameResult

    private var countOfRightAnswers = 0

    private var countOfQuestions = 0

    private lateinit var gameSettings: GameSettings
    private lateinit var level: Level
    fun startGame(level: Level) {
        getGameSettings(level)
        startTimer()
        generateQuestion()
    }

    private fun getGameSettings(level: Level) {
        this.level = level
        gameSettings = getGameSettingsUseCase(level)
        _minPercent.value = gameSettings.minPercentOfRightAnswers
    }

    private fun startTimer() {
        timer = object : CountDownTimer(
            gameSettings.gameTimeInSeconds * MILLIS_IN_SECONDS,
            MILLIS_IN_SECONDS
        ) {
            override fun onTick(millisUntilFinished: Long) {
                _formattedTime.value = formatTime(millisUntilFinished)
            }

            override fun onFinish() {
                finishGame()
            }
        }
        timer?.start()
    }

    private fun formatTime(millisUntilFinished: Long): String {
        val seconds = millisUntilFinished / MILLIS_IN_SECONDS
        val minutes = seconds / MINUTE_IN_SECONDS
        val leftSeconds = seconds - minutes * MINUTE_IN_SECONDS
        return String.format("%02d:%02d", minutes, leftSeconds)
    }

    private fun generateQuestion() {
        _question.value = generateQuestionUseCase(gameSettings.maxSumValue)
    }

    private fun updateProgress() {
        val percent = calculatePercent()
        _percentOfRightAnswers.value = percent
        _progressAnswers.value =
            String.format(
                context.resources.getString(R.string.progress_answers),
                countOfRightAnswers,
                gameSettings.minCountOfRightAnswers
            )
        _enoughRightAnswers.value = gameSettings.minCountOfRightAnswers <= countOfRightAnswers
        _enoughPercentRightAnswers.value = gameSettings.minPercentOfRightAnswers <= percent
        generateQuestion()
    }

    private fun calculatePercent(): Int {
        return ((countOfRightAnswers * 100.0) / countOfQuestions).toInt()
    }

    fun chooseAnswer(number: Int) {
        checkAnswer(number)
        generateQuestion()
    }

    private fun checkAnswer(number: Int) {
        val rightAnswer = question.value?.rightAnswer
        if (number == rightAnswer) {
            countOfRightAnswers++
        }
        countOfQuestions++
    }

    private fun finishGame() {
        timer?.onFinish()
        val winner = enoughRightAnswers.value == true && enoughPercentRightAnswers.value == true
        val gameResult = GameResult(winner, countOfRightAnswers, countOfQuestions, gameSettings)
        _gameResult.value = gameResult
    }

    override fun onCleared() {
        timer?.cancel()
        super.onCleared()
    }

    private companion object {
        private const val MILLIS_IN_SECONDS = 1000L
        private const val MINUTE_IN_SECONDS = 60
    }
}