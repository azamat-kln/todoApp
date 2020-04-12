package com.example.roomcoroutines

import android.app.Application
import com.example.roomcoroutines.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        configureDi()
    }

    private fun configureDi() {
        startKoin {
            // inject android context
            androidContext(this@MyApplication)
            modules(
                listOf(
                    viewModelModule,
                    repositoryModule,
                    noteDaoModule,
                    databaseModule,
                    roomCallBackModule,
                    coroutineScopeModule
                )
            )
        }
    }
}