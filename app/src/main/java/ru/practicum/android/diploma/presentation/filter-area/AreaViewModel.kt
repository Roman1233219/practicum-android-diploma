package ru.practicum.android.diploma.presentation.`filter-area`

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AreaViewModel : ViewModel() {

    private val _screenState = MutableLiveData<AreaUiState>(AreaUiState.Empty)
    fun observeState(): LiveData<AreaUiState> = _screenState

    // текущий выбор пользователя
    private var selectedCountry: AreaUi? = null
    private var selectedRegion: AreaUi? = null

    fun selectCountry(country: AreaUi?) {
        selectedCountry = country

        selectedRegion = null

        renderState()
    }

    fun selectRegion(region: AreaUi?) {
        Log.d("AREA", "selectRegion = ${region?.areaName}")
        selectedRegion = region
        renderState()
    }

    fun selectLocation(
        country: AreaUi?,
        region: AreaUi?
    ) {
        selectedCountry = country
        selectedRegion = region
        renderState()
    }

    private fun renderState() {
        if (selectedCountry == null && selectedRegion == null) {
            _screenState.value = AreaUiState.Empty
        } else {
            _screenState.value = AreaUiState.Content(
                country = selectedCountry,
                region = selectedRegion
            )
        }
    }
}
