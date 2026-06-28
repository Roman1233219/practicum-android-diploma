package ru.practicum.android.diploma.presentation.`filter-area`

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.practicum.android.diploma.R

class RegionViewModel : ViewModel() {
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
        // пока просто загружаю mock данные
        val areasList: List<AreaUi> = listOf(
            AreaUi(areaId = 1, areaName = "Москва"),
            AreaUi(areaId = 2, areaName = "Апрелевка"),
            AreaUi(areaId = 3, areaName = "Балашиха"),
            AreaUi(areaId = 4, areaName = "Бронницы"),
            AreaUi(areaId = 5, areaName = "Верея"),
            AreaUi(areaId = 6, areaName = "Видное"),
            AreaUi(areaId = 7, areaName = "Волоколамск"),
            AreaUi(areaId = 8, areaName = "Воскресенск"),
            AreaUi(areaId = 9, areaName = "Высоковск"),
            AreaUi(areaId = 10, areaName = "Голицыно"),

            AreaUi(areaId = 11, areaName = "Дедовск"),
            AreaUi(areaId = 12, areaName = "Дзержинский"),
            AreaUi(areaId = 13, areaName = "Дмитров"),
            AreaUi(areaId = 14, areaName = "Долгопрудный"),
            AreaUi(areaId = 15, areaName = "Домодедово"),
            AreaUi(areaId = 16, areaName = "Дубна"),
            AreaUi(areaId = 17, areaName = "Егорьевск"),
            AreaUi(areaId = 18, areaName = "Жуковский"),
            AreaUi(areaId = 19, areaName = "Зарайск"),
            AreaUi(areaId = 20, areaName = "Звенигород")
        )
        fullRegionsList = areasList

        renderScreenState(RegionUiState.Content(areasList))
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
