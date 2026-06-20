package ru.practicum.android.diploma.presentation.area

import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Job
import ru.practicum.android.diploma.R

class AreaViewModel : ViewModel() {
    private val screenState = MutableLiveData<AreaUiState>(AreaUiState.Initial)
    fun observeScreenState(): LiveData<AreaUiState> = screenState

    //полный список регионов
    private var fullAreasList: List<AreaUi> = emptyList()

    //для поисковой строки
    private var latestSearchText: String? = null
    private val handler = Handler(Looper.getMainLooper())

    init {
        loadAreas()
    }

    //первоначальная загрузка всех регионов
    private fun loadAreas() {
        //пока просто загружаю mock данные
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
        fullAreasList = areasList

        renderScreenState(AreaUiState.Content(areasList))
    }

    //поиск региона
    fun searchDebounce(changedText: String) {
        if (latestSearchText == changedText) return

        latestSearchText = changedText

        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)

        if (changedText.isBlank()) {
            renderScreenState(AreaUiState.Content(fullAreasList))
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
                AreaUiState.Content(fullAreasList)
            )
            return
        }

        val filteredList = fullAreasList.filter {
            it.areaName.contains(query, ignoreCase = true)
        }

        if (filteredList.isEmpty()) {
            renderScreenState(
                AreaUiState.Empty(R.string.placeholder_empty_area)
            )
        } else {
            renderScreenState(
                AreaUiState.Content(filteredList)
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
    }

    //изменение состояния экрана
    private fun renderScreenState(state: AreaUiState) {
        screenState.postValue(state)
    }

    private companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private val SEARCH_REQUEST_TOKEN = Any()
    }
}
