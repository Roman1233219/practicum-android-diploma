package ru.practicum.android.diploma.data.network

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import ru.practicum.android.diploma.data.NetworkClient
import ru.practicum.android.diploma.data.dto.FilterAreaRequest
import ru.practicum.android.diploma.data.dto.FilterAreaResponse
import ru.practicum.android.diploma.data.dto.FilterIndustriesRequest
import ru.practicum.android.diploma.data.dto.FilterIndustriesResponse
import ru.practicum.android.diploma.data.dto.Response
import ru.practicum.android.diploma.data.dto.VacanciesRequest
import ru.practicum.android.diploma.data.dto.VacancyDetailsRequest
import ru.practicum.android.diploma.data.dto.VacancyDetailsResponse
import ru.practicum.android.diploma.util.NetworkUtil

class RetrofitNetworkClient(
    private val apiService: PracticumApiService
) : NetworkClient {

    override suspend fun filterAreaRequest(dto: Any): Response {
        return when {
            !NetworkUtil.connectivityChecker() -> Response().apply { resultCode = NO_CONNECTION_CODE }
            dto !is FilterAreaRequest -> Response().apply { resultCode = BAD_REQUEST_CODE }
            else -> withContext(Dispatchers.IO) {
                try {
                    val list = apiService.getAreas()
                    Log.d("RetrofitNetworkClient", "getAreas success, size: ${list.size}")
                    FilterAreaResponse(results = list).apply { resultCode = SUCCESS_CODE }
                } catch (ex: HttpException) {
                    Log.e("RetrofitNetworkClient", "getAreas HttpException: ${ex.code()}")
                    Response().apply { resultCode = ex.code() }
                } catch (ex: Exception) {
                    Log.e("RetrofitNetworkClient", "getAreas Exception: ${ex.message}")
                    Response().apply { resultCode = SERVER_ERROR_CODE }
                }
            }
        }
    }

    override suspend fun filterIndustryRequest(dto: Any): Response {
        return when {
            !NetworkUtil.connectivityChecker() -> Response().apply { resultCode = NO_CONNECTION_CODE }
            dto !is FilterIndustriesRequest -> Response().apply { resultCode = BAD_REQUEST_CODE }
            else -> withContext(Dispatchers.IO) {
                try {
                    val list = apiService.getIndustries()
                    FilterIndustriesResponse(results = list).apply { resultCode = SUCCESS_CODE }
                } catch (ex: HttpException) {
                    Response().apply { resultCode = ex.code() }
                } catch (ex: Exception) {
                    Response().apply { resultCode = SERVER_ERROR_CODE }
                }
            }
        }
    }

    override suspend fun searchVacancies(dto: Any): Response {
        return when {
            !NetworkUtil.connectivityChecker() -> Response().apply { resultCode = NO_CONNECTION_CODE }
            dto !is VacanciesRequest -> Response().apply { resultCode = BAD_REQUEST_CODE }
            else -> executeRequest {
                val options = createSearchOptions(dto)
                apiService.searchVacancies(options)
            }
        }
    }

    override suspend fun getVacancyDetails(dto: Any): Response {
        return when {
            !NetworkUtil.connectivityChecker() -> Response().apply { resultCode = NO_CONNECTION_CODE }
            dto !is VacancyDetailsRequest -> Response().apply { resultCode = BAD_REQUEST_CODE }
            else -> executeRequest {
                val vacancyDto = apiService.getVacancyDetails(dto.vacancyId)
                VacancyDetailsResponse(vacancyDto)
            }
        }
    }

    private suspend fun executeRequest(request: suspend () -> Response): Response {
        return withContext(Dispatchers.IO) {
            try {
                request().apply { resultCode = SUCCESS_CODE }
            } catch (ex: HttpException) {
                Response().apply { resultCode = ex.code() }
            }
        }
    }

    private fun createSearchOptions(dto: VacanciesRequest): Map<String, String> {
        return mutableMapOf<String, String>().apply {
            put("text", dto.text)
            put("page", dto.page.toString())
            dto.salary?.let { put("salary", it.toString()) }
            if (dto.onlyWithSalary) {
                put("only_with_salary", "true")
            }
            dto.area?.let { put("area", it) }
            dto.industry?.let { put("industry", it) }
        }
    }

    companion object {
        private const val SUCCESS_CODE = 200
        private const val BAD_REQUEST_CODE = 400
        private const val SERVER_ERROR_CODE = 500
        private const val NO_CONNECTION_CODE = -1
    }
}
