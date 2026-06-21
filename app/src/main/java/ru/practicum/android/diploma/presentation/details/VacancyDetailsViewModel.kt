package ru.practicum.android.diploma.presentation.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.VacanciesInteractor
import ru.practicum.android.diploma.domain.models.ApiResult

class VacancyDetailsViewModel(
    private val vacancyId: String,
    private val interactor: VacanciesInteractor
) : ViewModel() {

    private val _state = MutableStateFlow<VacancyDetailsState>(VacancyDetailsState.Loading)
    val state: StateFlow<VacancyDetailsState> = _state.asStateFlow()

    init {
        loadVacancyDetails()
    }

    private fun loadVacancyDetails() {
        viewModelScope.launch {
            interactor.getVacancyDetails(vacancyId).collect { result ->
                when (result) {
                    is ApiResult.Loading -> {
                        _state.value = VacancyDetailsState.Loading
                    }
                    is ApiResult.Success -> {
                        _state.value = VacancyDetailsState.Content(result.data)
                    }
                    is ApiResult.Error -> {
                        _state.value = VacancyDetailsState.Error
                    }
                }
            }
        }
    }
}
