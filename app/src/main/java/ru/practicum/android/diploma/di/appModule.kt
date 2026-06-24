package ru.practicum.android.diploma.di_temp

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.koin.dsl.module

val appModule = module {
    single<Gson> { GsonBuilder().create() }
}
