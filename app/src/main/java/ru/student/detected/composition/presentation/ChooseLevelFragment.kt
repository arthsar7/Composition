package ru.student.detected.composition.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ru.student.detected.composition.databinding.FragmentChooseLevelBinding
import ru.student.detected.composition.domain.entity.Level

class ChooseLevelFragment : Fragment() {
    private var _binding: FragmentChooseLevelBinding? = null
    private val binding: FragmentChooseLevelBinding
        get() = _binding ?: throw RuntimeException("FragmentChooseLevelBinding == null")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChooseLevelBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleClicks()
    }

    private fun handleClicks() {
        with(binding) {
            buttonLevelTest.setOnClickListener {
                launchGameFragmentWithLevel(Level.TEST)
            }
            buttonLevelEasy.setOnClickListener {
                launchGameFragmentWithLevel(Level.EASY)
            }
            buttonLevelNormal.setOnClickListener {
                launchGameFragmentWithLevel(Level.NORMAL)
            }
            buttonLevelHard.setOnClickListener {
                launchGameFragmentWithLevel(Level.HARD)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun launchGameFragmentWithLevel(level: Level) {
        findNavController().navigate(
            ChooseLevelFragmentDirections.actionChooseLevelFragmentToGameFragment(level)
        )
    }
}