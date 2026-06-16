package ru.practicum.android.diploma.data.network

import retrofit2.http.GET
import ru.practicum.android.diploma.data.dto.FilterAreaResponse
import ru.practicum.android.diploma.data.dto.VacanciesResponse

interface PracticumApiService {
    @GET("/areas")
    suspend fun getAreas(): FilterAreaResponse

    @GET("/vacancies")
    suspend fun searchVacancies(text: String, page: Int): VacanciesResponse
}
