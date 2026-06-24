package ru.practicum.android.diploma

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import ru.practicum.android.diploma.di_temp.appModule
import ru.practicum.android.diploma.di_temp.databaseModule
import ru.practicum.android.diploma.di_temp.interactorModule
import ru.practicum.android.diploma.di_temp.networkModule
import ru.practicum.android.diploma.di_temp.repositoryModule
import ru.practicum.android.diploma.di_temp.viewModelModule

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            printLogger()
            androidContext(this@App)
            modules(
                listOf(
                    appModule,
                    databaseModule,
                    networkModule,
                    repositoryModule,
                    interactorModule,
                    viewModelModule
                )
            )
        }
    }
}
