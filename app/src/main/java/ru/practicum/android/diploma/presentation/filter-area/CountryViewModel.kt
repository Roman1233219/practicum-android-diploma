package ru.practicum.android.diploma.presentation.`filter-area`

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.domain.api.FilterAreaInteractor

class CountryViewModel(private val interactor: FilterAreaInteractor) : ViewModel() {

    private val _state = MutableLiveData<CountryUiState>()
    val state: LiveData<CountryUiState> = _state

    init {
        loadCountries()
    }

    private fun loadCountries() {
        _state.value = CountryUiState.Loading
        viewModelScope.launch {
            interactor.getAreas().collect { areas ->
                if (areas.isEmpty()) {
                    _state.postValue(CountryUiState.Error(R.string.placeholder_error_area))
                } else {
                    // Страны - это объекты верхнего уровня (те, у которых parentId == null)
                    // В ответе interactor.getAreas() приходят как раз объекты стран
                    val countries = areas.map { area ->
                        AreaUi(areaId = area.id, areaName = area.name, parentId = area.parentId)
                    }
                    _state.postValue(CountryUiState.Content(countries))
                }
            }
        }
    }
}
