package ru.practicum.android.diploma

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import ru.practicum.android.diploma.di.AppModule
import ru.practicum.android.diploma.di.DatabaseModule
import ru.practicum.android.diploma.di.InteractorModule
import ru.practicum.android.diploma.di.NetworkModule
import ru.practicum.android.diploma.di.RepositoryModule
import ru.practicum.android.diploma.di.ViewModelModule

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this

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
        lateinit var instance: App
            private set
    }
}
