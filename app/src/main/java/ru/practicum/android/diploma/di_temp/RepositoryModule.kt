package ru.practicum.android.diploma.di_temp

import org.koin.dsl.module
import ru.practicum.android.diploma.data.repository.AreasRepositoryImpl
import ru.practicum.android.diploma.data.repository.FavoritesRepositoryImpl
import ru.practicum.android.diploma.data.repository.FilterAreaRepositoryImpl
import ru.practicum.android.diploma.data.repository.FilterIndustriesRepositoryImpl
import ru.practicum.android.diploma.data.repository.FilterSettingsRepositoryImpl
import ru.practicum.android.diploma.data.repository.VacancyRepositoryImpl
import ru.practicum.android.diploma.domain.api.AreasRepository
import ru.practicum.android.diploma.domain.api.FavoritesRepository
import ru.practicum.android.diploma.domain.api.FilterAreaRepository
import ru.practicum.android.diploma.domain.api.FilterIndustriesRepository
import ru.practicum.android.diploma.domain.api.FilterSettingsRepository
import ru.practicum.android.diploma.domain.api.VacanciesRepository

val RepositoryModule = module {
    single<AreasRepository> {
        AreasRepositoryImpl(get())
    }

    single<VacanciesRepository> {
        VacancyRepositoryImpl(get())
    }

    single<FavoritesRepository> {
        FavoritesRepositoryImpl(get())
    }

    single<FilterSettingsRepository> {
        FilterSettingsRepositoryImpl(get(), get(), get())
    }

    single<FilterIndustriesRepository> {
        FilterIndustriesRepositoryImpl(get())
    }

    single<FilterAreaRepository> {
        FilterAreaRepositoryImpl(get())
    }

}
