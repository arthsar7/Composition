package ru.student.detected.composition.presentation

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ru.student.detected.composition.databinding.FragmentGameBinding
import ru.student.detected.composition.domain.entity.GameResult

class GameFragment : Fragment() {
    private var _binding: FragmentGameBinding? = null
    private val args by navArgs<GameFragmentArgs>()
    private val viewModel: GameViewModel by lazy {
        val viewModelFactory = GameViewModelFactory(requireActivity().application, args.level)
        ViewModelProvider(this, viewModelFactory)[GameViewModel::class.java]
    }
    private val binding: FragmentGameBinding
        get() = _binding ?: throw RuntimeException("FragmentGameBinding == null")


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
    }

    private fun observeViewModel() {
        with(viewModel) {
            question.observe(viewLifecycleOwner) { question ->
                val options = question.options
                binding.tvSum.text = question.sum.toString()
                binding.tvLeftNumber.text = question.visibleNumber.toString()
                val optionViews = mutableListOf(
                    binding.tvOption1,
                    binding.tvOption2,
                    binding.tvOption3,
                    binding.tvOption4,
                    binding.tvOption5,
                    binding.tvOption6
                )
                optionViews.forEachIndexed { index, tv ->
                    tv.text = options[index].toString()
                    tv.setOnClickListener {
                        viewModel.chooseAnswer(tv.text.toString().toInt())
                    }
                }
            }

            percentOfRightAnswers.observe(viewLifecycleOwner) {
                binding.progressBar.setProgress(it, true)
            }

            enoughRightAnswers.observe(viewLifecycleOwner) {
                binding.tvAnswersProgress.setTextColor(getColorByState(it))
            }
            progressAnswers.observe(viewLifecycleOwner) {
                binding.tvAnswersProgress.text = it
            }
            enoughPercentRightAnswers.observe(viewLifecycleOwner) {
                binding.progressBar.progressTintList = ColorStateList.valueOf(getColorByState(it))
            }

            formattedTime.observe(viewLifecycleOwner) {
                binding.tvTimer.text = it
            }

            minPercent.observe(viewLifecycleOwner) {
                binding.progressBar.secondaryProgress = it
            }

            gameResult.observe(viewLifecycleOwner) {
                launchGameFinishedFragment(gameResult = it)
            }
        }
    }


    private fun getColorByState(it: Boolean) = if (it) Color.GREEN else Color.RED

    private fun launchGameFinishedFragment(gameResult: GameResult) {
        findNavController().navigate(
            GameFragmentDirections.actionGameFragmentToGameFinishedFragment(gameResult)
        )
    }
}