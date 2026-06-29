package ru.practicum.android.diploma.presentation.`filter-area`

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.FilterAreaInteractor
import ru.practicum.android.diploma.domain.models.Area

class AreaViewModel(
    private val areaInteractor: FilterAreaInteractor
) : ViewModel() {

    private val _screenState = MutableLiveData<AreaUiState>(AreaUiState.Empty)
    fun observeState(): LiveData<AreaUiState> = _screenState

    private var selectedCountry: AreaUi? = null
    private var selectedRegion: AreaUi? = null

    fun selectCountry(country: AreaUi?) {
        selectedCountry = country
        // При смене страны всегда сбрасываем регион (согласно ТЗ)
        selectedRegion = null
        renderState()
    }

    fun selectRegion(region: AreaUi?) {
        selectedRegion = region
        if (region != null && selectedCountry == null) {
            // Если страна не выбрана, пытаемся найти её по parentId региона
            findCountryByRegion(region)
        } else {
            renderState()
        }
    }

    fun selectLocation(
        country: AreaUi?,
        region: AreaUi?
    ) {
        selectedCountry = country
        selectedRegion = region
        renderState()
    }

    private fun findCountryByRegion(region: AreaUi) {
        viewModelScope.launch {
            areaInteractor.getAreas().collect { countries ->
                val country = findParentCountry(countries, region)
                if (country != null) {
                    selectedCountry = AreaUi(
                        areaId = country.id,
                        areaName = country.name,
                        parentId = null
                    )
                }
                renderState()
            }
        }
    }

    private fun findParentCountry(countries: List<Area>, region: AreaUi): Area? {
        // Проходим по каждой стране и ищем в её дереве нужный регион
        for (country in countries) {
            if (isRegionInCountry(country, region.areaId)) {
                return country
            }
        }
        return null
    }

    private fun isRegionInCountry(parent: Area, regionId: Int): Boolean {
        if (parent.id == regionId) return true
        for (child in parent.areas) {
            if (isRegionInCountry(child, regionId)) return true
        }
        return false
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
