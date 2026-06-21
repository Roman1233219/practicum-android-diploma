package ru.practicum.android.diploma.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentVacancyDetailsBinding
import ru.practicum.android.diploma.presentation.details.VacancyDetailsState
import ru.practicum.android.diploma.presentation.details.VacancyDetailsViewModel

class VacancyDetailsFragment : Fragment() {
    private var _binding: FragmentVacancyDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: VacancyDetailsViewModel by viewModel {
        parametersOf("dummy_id") // Временно хардкодим ID для этапа "Logic Start"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentVacancyDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupServerErrorState()

        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state.collect { state ->
                render(state)
            }
        }
    }

    private fun render(state: VacancyDetailsState) {
        when (state) {
            is VacancyDetailsState.Loading -> {
                // Логика показа загрузки
            }
            is VacancyDetailsState.Content -> {
                binding.vacancyTitle.text = state.vacancy.vacancyName
                binding.layoutServerError.root.isVisible = false
                binding.vacancyContent.isVisible = true
            }
            is VacancyDetailsState.Error -> {
                showServerError()
            }
        }
    }

    private fun setupServerErrorState() {
        binding.layoutServerError.ivPlaceholderPicture.setImageResource(R.drawable.placeholder_server_error_vacancy)
        binding.layoutServerError.tvPlaceholderText.text = getString(R.string.server_error)
        binding.layoutServerError.tvPlaceholderText.isVisible = true
    }

    private fun showServerError() {
        binding.layoutServerError.root.isVisible = true
        binding.vacancyContent.isVisible = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
