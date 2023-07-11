package ru.student.detected.composition.presentation

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
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        observeViewModel()
    }

    private fun observeViewModel() {
        with(viewModel) {
            gameResult.observe(viewLifecycleOwner) {
                launchGameFinishedFragment(gameResult = it)
            }
        }
    }


    private fun launchGameFinishedFragment(gameResult: GameResult) {
        findNavController().navigate(
            GameFragmentDirections.actionGameFragmentToGameFinishedFragment(gameResult)
        )
    }
}