package ru.practicum.android.diploma.presentation.`filter-area`

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.domain.api.FilterAreaInteractor
import ru.practicum.android.diploma.domain.models.Area

class RegionViewModel(
    private val countryId: Int,
    private val interactor: FilterAreaInteractor
) : ViewModel() {
    private val screenState = MutableLiveData<RegionUiState>(RegionUiState.Initial)
    fun observeScreenState(): LiveData<RegionUiState> = screenState

    // полный список регионов
    private var fullRegionsList: List<AreaUi> = emptyList()

    // для поисковой строки
    private var latestSearchText: String? = null
    private val handler = Handler(Looper.getMainLooper())

    init {
        loadAreas()
    }

    // первоначальная загрузка всех регионов
    private fun loadAreas() {
        renderScreenState(RegionUiState.Initial)
        viewModelScope.launch {
            interactor.getAreas().collect { areas ->
                if (areas.isEmpty()) {
                    renderScreenState(RegionUiState.Error(R.string.placeholder_error_area))
                } else {
                    val allRegions = mutableListOf<AreaUi>()

                    if (countryId != -1) {
                        // Если страна выбрана, ищем её и берем только её регионы
                        val country = areas.find { it.id == countryId }
                        if (country != null) {
                            allRegions.addAll(flattenAreas(country.areas))
                        }
                    } else {
                        // Если страна не выбрана, собираем регионы всех стран
                        areas.forEach { country ->
                            allRegions.addAll(flattenAreas(country.areas))
                        }
                    }

                    fullRegionsList = allRegions.distinctBy { it.areaId }
                        .sortedBy { it.areaName }
                    renderScreenState(RegionUiState.Content(fullRegionsList))
                }
            }
        }
    }

    private fun flattenAreas(areas: List<Area>): List<AreaUi> {
        val result = mutableListOf<AreaUi>()
        for (area in areas) {
            result.add(AreaUi(areaId = area.id, areaName = area.name, parentId = area.parentId))
            if (area.areas.isNotEmpty()) {
                result.addAll(flattenAreas(area.areas))
            }
        }
        return result
    }

    // поиск региона
    fun searchDebounce(changedText: String) {
        if (latestSearchText == changedText) return

        latestSearchText = changedText

        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)

        if (changedText.isBlank()) {
            renderScreenState(RegionUiState.Content(fullRegionsList))
            return
        }

        handler.postDelayed(
            { search(changedText) },
            SEARCH_DEBOUNCE_DELAY
        )
    }

    private fun search(query: String) {
        if (query.isBlank()) {
            renderScreenState(
                RegionUiState.Content(fullRegionsList)
            )
            return
        }

        val filteredList = fullRegionsList.filter {
            it.areaName.contains(query, ignoreCase = true)
        }

        if (filteredList.isEmpty()) {
            renderScreenState(
                RegionUiState.Empty(R.string.placeholder_empty_area)
            )
        } else {
            renderScreenState(
                RegionUiState.Content(filteredList)
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
    }

    // изменение состояния экрана
    private fun renderScreenState(state: RegionUiState) {
        screenState.postValue(state)
    }

    private companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private val SEARCH_REQUEST_TOKEN = Any()
    }
}
