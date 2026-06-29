package ru.practicum.android.diploma.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.launch
import androidx.lifecycle.viewModelScope
import ru.practicum.android.diploma.domain.api.FilterIndustriesInteractor
import ru.practicum.android.diploma.domain.models.Industry
import ru.practicum.android.diploma.util.debounce

class IndustryViewModel(val interactor: FilterIndustriesInteractor) : ViewModel() {
    private val liveData = MutableLiveData<IndustryState>(IndustryState.IsLoading)
    fun observeLiveData(): LiveData<IndustryState> = liveData

    private var allIndustries: List<Industry> = emptyList()
    private var lastSearchRequest: String = ""
    private var selectedIndustry: Industry? = null

    private val searchDebounceFunction = debounce<String>(SEARCH_DEBOUNCE_DELAY, viewModelScope, true) { query ->
        filterIndustries(query)
    }

    init {
        loadIndustries()
    }

    private fun loadIndustries() {
        viewModelScope.launch {
            try {
                renderState(IndustryState.IsLoading)
                interactor.getIndustries().collect { industries ->
                    // Выпрямляем список, так как API может возвращать вложенные структуры
                    // Но судя по IndustryConverter, там уже плоский список
                    allIndustries = industries.sortedBy { it.industryName }
                    if (allIndustries.isEmpty()) {
                        renderState(IndustryState.Empty(""))
                    } else {
                        renderState(IndustryState.Content(allIndustries))
                    }
                }
            } catch (e: Exception) {
                renderState(IndustryState.Error(""))
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
