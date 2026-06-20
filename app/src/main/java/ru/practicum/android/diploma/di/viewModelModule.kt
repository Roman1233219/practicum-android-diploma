package ru.practicum.android.diploma.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.practicum.android.diploma.presentation.area.AreaViewModel

val viewModelModule = module {
    //заглушка удалена - ставить вызов реальной вьюмодели
    viewModel {
        AreaViewModel()
    }
}
