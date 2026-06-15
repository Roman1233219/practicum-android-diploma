package ru.practicum.android.diploma

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module
import ru.practicum.android.diploma.di.AppModule
import ru.practicum.android.diploma.di.DataBaseModule
import ru.practicum.android.diploma.di.NetworkModule

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(module { includes(AppModule, DataBaseModule, NetworkModule) })
        }
    }
}
