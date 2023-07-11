package ru.student.detected.composition.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ru.student.detected.composition.R
import ru.student.detected.composition.databinding.FragmentGameFinishedBinding
import ru.student.detected.composition.domain.entity.GameResult

class GameFinishedFragment : Fragment() {
    private var _binding: FragmentGameFinishedBinding? = null
    private val gameResult: GameResult by lazy {
        GameFinishedFragmentArgs.fromBundle(requireArguments()).gameResult
    }
    private val binding: FragmentGameFinishedBinding
        get() = _binding ?: throw RuntimeException("FragmentGameFinishedBinding == null")

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


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun retryGame() {
        findNavController().popBackStack()
    }
}