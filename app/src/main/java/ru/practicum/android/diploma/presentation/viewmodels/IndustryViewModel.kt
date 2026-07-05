package ru.practicum.android.diploma.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.data.network.models.HttpErrorType
import ru.practicum.android.diploma.data.network.models.toHttpErrorType
import ru.practicum.android.diploma.domain.api.FilterIndustriesInteractor
import ru.practicum.android.diploma.domain.api.FilterSettingsInteractor
import ru.practicum.android.diploma.domain.models.ApiResult
import ru.practicum.android.diploma.domain.models.Industry
import ru.practicum.android.diploma.util.debounce

class IndustryViewModel(
    val interactor: FilterIndustriesInteractor,
    private val filterSettingsInteractor: FilterSettingsInteractor
) : ViewModel() {
    private val liveData = MutableLiveData<IndustryState>(IndustryState.IsLoading)
    fun observeLiveData(): LiveData<IndustryState> = liveData

    private val _selectedIndustryId = MutableLiveData<String?>(null)
    val selectedIndustryId: LiveData<String?> = _selectedIndustryId

    private var allIndustries: List<Industry> = emptyList()
    private var lastSearchRequest: String = ""
    private var selectedIndustry: Industry? = null

    private val searchDebounceFunction = debounce<String>(SEARCH_DEBOUNCE_DELAY, viewModelScope, true) { query ->
        filterIndustries(query)
    }

    init {
        loadSettings()
        loadIndustries()
    }

    private fun loadSettings() {
        viewModelScope.launch {
            val settings = filterSettingsInteractor.getTempFilterFlow().first()
            _selectedIndustryId.value = settings.industryId
        }
    }

    private fun loadIndustries() {
        viewModelScope.launch {
            renderState(IndustryState.IsLoading)
            interactor.getIndustries().collect { result ->
                when (result) {
                    is ApiResult.Success -> {
                        allIndustries = result.data.sortedBy { it.industryName }
                        if (allIndustries.isEmpty()) {
                            renderState(IndustryState.Empty(""))
                        } else {
                            renderState(IndustryState.Content(allIndustries))
                        }
                    }

                    is ApiResult.Error -> {
                        if (result.httpCode.toHttpErrorType() == HttpErrorType.NETWORK) {
                            renderState(IndustryState.NoInternet)
                        } else {
                            renderState(IndustryState.Error(""))
                        }
                    }

                    else -> Unit
                }
            }
        }
    }

    fun searchDebounce(searchQuery: String) {
        if (searchQuery == lastSearchRequest) {
            return
        }
        lastSearchRequest = searchQuery
        searchDebounceFunction(searchQuery)
    }

    private fun filterIndustries(query: String) {
        if (query.isEmpty()) {
            renderState(IndustryState.Content(allIndustries))
        } else {
            val filtered = allIndustries.filter {
                it.industryName.contains(query, ignoreCase = true)
            }
            if (filtered.isEmpty()) {
                renderState(IndustryState.Empty(""))
            } else {
                renderState(IndustryState.Content(filtered))
            }
        }
    }

    fun selectIndustry(industry: Industry) {
        selectedIndustry = if (selectedIndustry == industry) null else industry
        // Перерисовываем текущее состояние с учетом выбора (если нужно)
        // Но сейчас выбор хранится в адаптере для визуализации
    }

    fun getSelectedIndustry(): Industry? = selectedIndustry

    private fun renderState(state: IndustryState) {
        liveData.postValue(state)
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 500L
    }
}
