package ru.student.detected.composition.presentation

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import ru.student.detected.composition.R
import ru.student.detected.composition.databinding.FragmentGameFinishedBinding
import ru.student.detected.composition.domain.entity.GameResult

class GameFinishedFragment : Fragment() {
    private var _binding: FragmentGameFinishedBinding? = null
    private lateinit var gameResult: GameResult
    private val binding: FragmentGameFinishedBinding
        get() = _binding ?: throw RuntimeException("FragmentGameFinishedBinding == null")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArgs()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameFinishedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleBackToRetry()
        setGameResultViews()

    }

    private fun setGameResultViews() {
        with(binding) {
            buttonRetry.setOnClickListener {
                retryGame()
            }

            tvRequiredAnswers.text =
                String.format(
                    getString(R.string.required_score),
                    gameResult.gameSettings.minCountOfRightAnswers
                )

            tvRequiredPercentage.text =
                String.format(
                    getString(R.string.required_percentage),
                    gameResult.gameSettings.minPercentOfRightAnswers
                )

            tvScoreAnswers.text =
                String.format(
                    getString(R.string.score_answers),
                    gameResult.countOfRightAnswers
                )

            tvScorePercentage.text =
                String.format(
                    getString(R.string.score_percentage),
                    getScorePercentage()
                )

            emojiResult.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    getEmojiDrawable()
                )
            )
        }
    }

    private fun getEmojiDrawable() =
        if (gameResult.winner) R.drawable.ic_smile else R.drawable.ic_sad

    private fun getScorePercentage(): Int {
        return if (gameResult.countOfQuestions != 0)
            ((gameResult.countOfRightAnswers * 100.0) / gameResult.countOfQuestions).toInt()
        else 0

    }

    private fun handleBackToRetry() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    retryGame()
                }
            })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun parseArgs() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            requireArguments().getParcelable<GameResult>(KEY_GAME_RESULT)?.let {
                gameResult = it
            }
        } else {
            requireArguments().getParcelable(KEY_GAME_RESULT, GameResult::class.java)?.let {
                gameResult = it
            }
        }
    }

    private fun retryGame() {
        requireActivity().supportFragmentManager.popBackStack(
            GameFragment.NAME,
            FragmentManager.POP_BACK_STACK_INCLUSIVE
        )
    }

    companion object {
        private const val KEY_GAME_RESULT = "GAME_RESULT"

        fun newInstance(gameResult: GameResult): GameFinishedFragment {
            return GameFinishedFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(KEY_GAME_RESULT, gameResult)
                }
            }
        }
    }
}