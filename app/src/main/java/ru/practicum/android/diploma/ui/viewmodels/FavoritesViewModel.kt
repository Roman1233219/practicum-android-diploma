package ru.practicum.android.diploma.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.models.VacancyCard

class FavoritesViewModel {
    private val stateLiveData = MutableLiveData<FavoritesState>()
    fun observeState(): LiveData<FavoritesState> = stateLiveData

    init {
        fillData()
    }

    fun fillData() {
        viewModelScope.launch {
            //interactor.favoritesVacancy().collect { vacancy -> processResult(vacancy) }
        }
    }

    fun processResult(vacancy: List<VacancyCard>) {
        when {
            (vacancy.isEmpty()) -> {
                renderState(
                    FavoritesState.IsEmpty(true)
                )
            }

            (vacancy.isNotEmpty()) -> {
                renderState(FavoritesState.Content(vacancy))
            }

            else -> {
                renderState(FavoritesState.ConnectionError(true))
            }
        }
    }

    private fun renderState(state: FavoritesState) {
        stateLiveData.postValue(state)
    }
}
