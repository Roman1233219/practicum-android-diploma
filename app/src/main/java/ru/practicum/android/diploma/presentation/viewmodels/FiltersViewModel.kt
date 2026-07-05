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
            // 1. Инициализируем временный фильтр (копируем из постоянного, если он пуст)
            // Но по ТЗ "сохраняются автоматически", значит при входе мы должны видеть
            // то, что не досохранили в прошлый раз.
            // Поэтому просто подписываемся на временный фильтр.

            // Загружаем постоянный фильтр для сравнения (canApply)
            launch {
                interactor.getFilterFlow().collect { settings ->
                    initialSettings = settings
                    updateState()
                }
            }

            // Подписываемся на временный фильтр для отображения и редактирования
            launch {
                interactor.getTempFilterFlow().collect { settings ->
                    currentSettings = settings
                    updateState()
                }
            }
        }
    }

    fun setSalary(salary: String?) {
        val salaryInt = salary?.toIntOrNull()
        if (currentSettings.expectedSalary != salaryInt) {
            saveTempSettings(currentSettings.copy(expectedSalary = salaryInt))
        }
    }

    fun setNotShowWithoutSalary(checked: Boolean) {
        if (currentSettings.notShowWithoutSalary != checked) {
            saveTempSettings(currentSettings.copy(notShowWithoutSalary = checked))
        }
    }

    fun applyFilters() {
        viewModelScope.launch {
            interactor.applyTempFilter()
        }
    }

    fun resetFilters() {
        viewModelScope.launch {
            interactor.clearTempFilter()
            interactor.saveFilterSettings(FilterSettings())
        }
    }

    fun clearWorkPlace() {
        saveTempSettings(currentSettings.copy(
            countryId = null,
            countryName = null,
            regionId = null,
            regionName = null
        ))
    }

    fun clearIndustry() {
        saveTempSettings(currentSettings.copy(
            industryId = null,
            industryName = null
        ))
    }

    fun setIndustry(industryName: String?, industryId: String?) {
        if (currentSettings.industryName != industryName || currentSettings.industryId != industryId) {
            saveTempSettings(currentSettings.copy(
                industryName = industryName,
                industryId = industryId
            ))
        }
    }

    fun setArea(countryName: String?, countryId: Int?, regionName: String?, regionId: Int?) {
        // Отсекаем "0", которые прилетают из-за особенностей getInt в Bundle
        val countryIdStr = if (countryId == null || countryId == 0) null else countryId.toString()
        val regionIdStr = if (regionId == null || regionId == 0) null else regionId.toString()

        val finalCountryName = if (countryIdStr == null) null else countryName
        val finalRegionName = if (regionIdStr == null) null else regionName

        if (currentSettings.countryName != finalCountryName || currentSettings.countryId != countryIdStr ||
            currentSettings.regionName != finalRegionName || currentSettings.regionId != regionIdStr
        ) {
            saveTempSettings(currentSettings.copy(
                countryName = finalCountryName,
                countryId = countryIdStr,
                regionName = finalRegionName,
                regionId = regionIdStr
            ))
        }
    }

    private fun saveTempSettings(settings: FilterSettings) {
        viewModelScope.launch {
            interactor.saveTempFilter(settings)

            // Если после изменения временного фильтра он стал пустым,
            // мы должны очистить и основной фильтр, так как кнопка "Применить" скроется.
            val hasAnyFilter = !settings.countryId.isNullOrBlank() && settings.countryId != "0" ||
                !settings.regionId.isNullOrBlank() && settings.regionId != "0" ||
                !settings.industryId.isNullOrBlank() ||
                settings.expectedSalary != null ||
                settings.notShowWithoutSalary

            if (!hasAnyFilter) {
                interactor.saveFilterSettings(FilterSettings())
            }
        }
    }

    private fun updateState() {
        val hasAnyFilter = !currentSettings.countryId.isNullOrBlank() && currentSettings.countryId != "0" ||
            !currentSettings.regionId.isNullOrBlank() && currentSettings.regionId != "0" ||
            !currentSettings.industryId.isNullOrBlank() ||
            currentSettings.expectedSalary != null ||
            currentSettings.notShowWithoutSalary

        val canApply = hasAnyFilter
        val canReset = hasAnyFilter
        _state.value = FiltersState.Content(
            settings = currentSettings,
            canApply = canApply,
            canReset = canReset
        )
    }
}
