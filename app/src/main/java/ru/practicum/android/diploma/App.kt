package ru.practicum.android.diploma

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import ru.practicum.android.diploma.di_temp.AppModule
import ru.practicum.android.diploma.di_temp.DatabaseModule
import ru.practicum.android.diploma.di_temp.InteractorModule
import ru.practicum.android.diploma.di_temp.NetworkModule
import ru.practicum.android.diploma.di_temp.RepositoryModule
import ru.practicum.android.diploma.di_temp.ViewModelModule

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        _instance = this

        startKoin {
            printLogger()
            androidContext(this@App)
            modules(
                listOf(
                    AppModule,
                    DatabaseModule,
                    NetworkModule,
                    RepositoryModule,
                    InteractorModule,
                    ViewModelModule
                )
            )
        }
    }

    companion object {
        private var _instance: App? = null
        val instance: App get() = _instance ?: error("App instance not initialized")
    }
}
