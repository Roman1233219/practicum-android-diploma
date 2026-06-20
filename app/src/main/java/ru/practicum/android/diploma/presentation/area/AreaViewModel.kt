package ru.practicum.android.diploma.presentation.area

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AreaViewModel : ViewModel() {
    private val screenState = MutableLiveData<AreaUiState>(AreaUiState.Initial)
    fun observeScreenState(): LiveData<AreaUiState> = screenState

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
        renderScreenState(AreaUiState.Content(areasList))
    }

    //изменение состояния экрана
    private fun renderScreenState(state: AreaUiState) {
        screenState.postValue(state)
    }
}
