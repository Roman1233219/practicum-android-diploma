package ru.practicum.android.diploma.data.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import ru.practicum.android.diploma.data.dto.SomeResponse

interface SomeApiService {
    @GET("getSomething")
    suspend fun getSomething(@Query("term") text: String): SomeResponse
}
