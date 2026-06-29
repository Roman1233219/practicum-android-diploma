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
        val countryIdStr = countryId?.toString()
        val regionIdStr = regionId?.toString()
        if (currentSettings.countryName != countryName || currentSettings.countryId != countryIdStr ||
            currentSettings.regionName != regionName || currentSettings.regionId != regionIdStr
        ) {
            saveTempSettings(currentSettings.copy(
                countryName = countryName,
                countryId = countryIdStr,
                regionName = regionName,
                regionId = regionIdStr
            ))
        }
    }

    private fun saveTempSettings(settings: FilterSettings) {
        viewModelScope.launch {
            interactor.saveTempFilter(settings)
        }
    }

    private fun updateState() {
        val hasAnyFilter = !currentSettings.countryId.isNullOrBlank() && currentSettings.countryId != "0" ||
            !currentSettings.regionId.isNullOrBlank() && currentSettings.regionId != "0" ||
            !currentSettings.industryId.isNullOrBlank() ||
            currentSettings.expectedSalary != null ||
            currentSettings.notShowWithoutSalary

        // Если экран стал пустым — принудительно очищаем и основной фильтр в БД, чтобы погасла иконка
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
