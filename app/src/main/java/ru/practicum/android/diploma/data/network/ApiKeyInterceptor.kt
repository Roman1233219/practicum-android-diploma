package ru.practicum.android.diploma.data.network

import okhttp3.Interceptor
import okhttp3.Response

class ApiKeyInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val modifiedRequest = originalRequest.newBuilder()
            .header("Authorization: ", BuildConfig.API_KEY)
            .build()
        return chain.proceed(modifiedRequest)
    }
}
