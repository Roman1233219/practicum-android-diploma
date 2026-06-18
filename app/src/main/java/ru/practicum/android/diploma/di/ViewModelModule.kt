package ru.practicum.android.diploma.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.practicum.android.diploma.presentation.SomeViewModel

val ViewModelModule = module {
    //viewModel { SomeViewModel(get()) } заглушка удалена - ставить вызов реальной вьюмодели
}
