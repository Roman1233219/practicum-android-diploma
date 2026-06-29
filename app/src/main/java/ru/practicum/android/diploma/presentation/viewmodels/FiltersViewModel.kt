package ru.practicum.android.diploma.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.FilterSettingsInteractor
import ru.practicum.android.diploma.domain.models.FilterSettings

class FiltersViewModel(
    private val interactor: FilterSettingsInteractor
) : ViewModel() {

    private val _state = MutableLiveData<FiltersState>()
    val state: LiveData<FiltersState> = _state

    private var currentSettings: FilterSettings = FilterSettings()
    private var initialSettings: FilterSettings = FilterSettings()

    init {
        loadSettings()
    }

    fun loadSettings() {
        _state.value = FiltersState.Loading
        viewModelScope.launch {
            // Загружаем актуальные настройки из основного фильтра Room
            val settings = interactor.getFilterSettings()
            initialSettings = settings
            currentSettings = settings
            updateState()
        }
    }

    fun setSalary(salary: String?) {
        val salaryInt = salary?.toIntOrNull()
        if (currentSettings.expectedSalary != salaryInt) {
            currentSettings = currentSettings.copy(expectedSalary = salaryInt)
            updateState()
        }
    }

    fun setNotShowWithoutSalary(checked: Boolean) {
        if (currentSettings.notShowWithoutSalary != checked) {
            currentSettings = currentSettings.copy(notShowWithoutSalary = checked)
            updateState()
        }
    }

    fun applyFilters() {
        viewModelScope.launch {
            // Сохраняем сразу в основной фильтр в Room
            interactor.saveFilterSettings(currentSettings)
            initialSettings = currentSettings
            updateState()
        }
    }

    fun resetFilters() {
        currentSettings = FilterSettings()
        updateState()
    }

    fun clearWorkPlace() {
        currentSettings = currentSettings.copy(
            countryId = null,
            countryName = null,
            regionId = null,
            regionName = null
        )
        updateState()
    }

    fun clearIndustry() {
        currentSettings = currentSettings.copy(
            industryId = null,
            industryName = null
        )
        updateState()
    }

    fun setIndustry(industryName: String?, industryId: String?) {
        if (currentSettings.industryName != industryName || currentSettings.industryId != industryId) {
            currentSettings = currentSettings.copy(
                industryName = industryName,
                industryId = industryId
            )
            updateState()
        }
    }

    fun setArea(countryName: String?, countryId: Int?, regionName: String?, regionId: Int?) {
        val countryIdStr = countryId?.toString()
        val regionIdStr = regionId?.toString()
        if (currentSettings.countryName != countryName || currentSettings.countryId != countryIdStr ||
            currentSettings.regionName != regionName || currentSettings.regionId != regionIdStr
        ) {
            currentSettings = currentSettings.copy(
                countryName = countryName,
                countryId = countryIdStr,
                regionName = regionName,
                regionId = regionIdStr
            )
            updateState()
        }
    }

    private fun updateState() {
        val hasAnyFilter = currentSettings.countryId != null ||
            currentSettings.regionId != null ||
            currentSettings.industryId != null ||
            currentSettings.expectedSalary != null ||
            currentSettings.notShowWithoutSalary

        if (!hasAnyFilter) {
            viewModelScope.launch {
                interactor.saveFilterSettings(FilterSettings())
            }
        }

        val canApply = hasAnyFilter
        val canReset = hasAnyFilter
        _state.value = FiltersState.Content(
            settings = currentSettings,
            canApply = canApply,
            canReset = canReset
        )
    }
}
