package com.example.morsecode

import android.app.Application
import com.example.morsecode.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MorseCodeApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MorseCodeApplication)
            modules(appModule)
        }
    }
}
