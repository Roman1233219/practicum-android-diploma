package ru.practicum.android.diploma.data.network

import retrofit2.http.GET
import ru.practicum.android.diploma.data.dto.SalaryDto
import ru.practicum.android.diploma.data.dto.VacancyDetailResponse
import ru.practicum.android.diploma.data.dto.VacancyResponse

interface PracticumApiService {
    @GET("vacancies/{id}")
    suspend fun getVacancyById(@Path("id") id: String): VacancyDetailResponse
    @GET("vacancies")
    suspend fun getVacancies(
        @Query("area") area: String? = null,
        @Query("industry") industry: String? = null,
        @Query("text") text: String? = null,
        @Query("salary") expectedSalary: Int? = null,
        @Query("page") page: Int? = null,
    ): VacancyResponse
}
