package com.example.vettrack.core

import android.app.Application
import com.example.vettrack.di.presentationModule
import com.example.vettrack.di.repositoryModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())
        Timber.d("MyApp_TAG: onCreate: ")

        startKoin {
            androidContext(this@MyApp)
            modules(presentationModule + repositoryModule)
        }
    }


}