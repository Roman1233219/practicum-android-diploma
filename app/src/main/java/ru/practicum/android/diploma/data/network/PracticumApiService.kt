package ru.practicum.android.diploma.data.network

import retrofit2.http.GET
import retrofit2.http.Query
import ru.practicum.android.diploma.data.dto.FilterAreaResponse
import ru.practicum.android.diploma.data.dto.VacanciesResponse

interface PracticumApiService {
    @GET("/areas")
    suspend fun getAreas(): FilterAreaResponse

    @GET("/vacancies")
    suspend fun searchVacancies(@Query("id") text: String,
                                @Query("id") page: Int
    ): VacanciesResponse
}
