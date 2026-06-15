package ru.practicum.android.diploma.data

import ru.practicum.android.diploma.data.dto.Response

interface NetworkClient {
    suspend fun vacancyDetailRequest(dto: Any): Response
    suspend fun vacancyRequest(dto: Any): Response
}
