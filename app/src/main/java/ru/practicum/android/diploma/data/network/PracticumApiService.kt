package ru.practicum.android.diploma.data.network

import retrofit2.http.GET
import ru.practicum.android.diploma.data.dto.VacancyDetailResponse

interface PracticumApiService {
    @GET("vacancies/{id}")
    suspend fun getVacancyById(@Path("id") id: String): VacancyDetailResponse
}
