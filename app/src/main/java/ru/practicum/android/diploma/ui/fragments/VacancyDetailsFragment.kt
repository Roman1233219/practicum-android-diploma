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
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.presentation.details.VacancyDetailsState
import ru.practicum.android.diploma.presentation.details.VacancyDetailsViewModel
import ru.practicum.android.diploma.util.HtmlUtils

class VacancyDetailsFragment : Fragment() {
    private var _binding: FragmentVacancyDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: VacancyDetailsViewModel by viewModel {
        parametersOf("dummy_id") // Временно хардкодим ID
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
                // Здесь можно добавить показ лоадера
                binding.vacancyContent.isVisible = false
                binding.layoutServerError.root.isVisible = false
            }
            is VacancyDetailsState.Content -> {
                showContent(state.vacancy)
            }
            is VacancyDetailsState.Error -> {
                showServerError()
            }
        }
    }

    private fun showContent(vacancy: Vacancy) {
        binding.layoutServerError.root.isVisible = false
        binding.vacancyContent.isVisible = true

        with(binding) {
            tvVacancyName.text = vacancy.vacancyName
            tvCompanyName.text = vacancy.companyName
            tvArea.text = vacancy.areaName
            tvExperience.text = vacancy.experienceName
            
            // Обработка зарплаты
            tvSalary.text = formatSalary(vacancy)
            
            // Самое важное: парсинг HTML описания
            tvDescription.text = HtmlUtils.parseHtml(vacancy.description)
        }
    }

    private fun formatSalary(vacancy: Vacancy): String {
        return when {
            vacancy.salaryFrom != null && vacancy.salaryTo != null -> {
                "От ${vacancy.salaryFrom} до ${vacancy.salaryTo} ${vacancy.currency}"
            }
            vacancy.salaryFrom != null -> {
                "От ${vacancy.salaryFrom} ${vacancy.currency}"
            }
            vacancy.salaryTo != null -> {
                "До ${vacancy.salaryTo} ${vacancy.currency}"
            }
            else -> "Зарплата не указана"
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
