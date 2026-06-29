package ru.practicum.android.diploma.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.practicum.android.diploma.presentation.`filter-area`.AreaViewModel
import ru.practicum.android.diploma.presentation.`filter-area`.CountryViewModel
import ru.practicum.android.diploma.presentation.`filter-area`.RegionViewModel
import ru.practicum.android.diploma.presentation.viewmodels.CountrySelectionViewModel
import ru.practicum.android.diploma.presentation.viewmodels.FavouritesViewModel
import ru.practicum.android.diploma.presentation.viewmodels.FiltersViewModel
import ru.practicum.android.diploma.presentation.viewmodels.IndustryViewModel
import ru.practicum.android.diploma.presentation.viewmodels.SearchViewModel
import ru.practicum.android.diploma.presentation.viewmodels.details.VacancyDetailsViewModel

val ViewModelModule = module {
    viewModel { SearchViewModel(get(), get()) }

    viewModel { FavouritesViewModel(get()) }

    viewModel { FiltersViewModel(get()) }

    viewModel { CountrySelectionViewModel(get(), get()) }

    viewModel { AreaViewModel(get(), get()) }

    viewModel { (countryId: Int) -> RegionViewModel(countryId, get()) }

    viewModel { CountryViewModel(get()) }

    viewModel { (vacancyId: String) ->
        VacancyDetailsViewModel(vacancyId, get(), get())
    }

    viewModel { IndustryViewModel(get(), get()) }
}
