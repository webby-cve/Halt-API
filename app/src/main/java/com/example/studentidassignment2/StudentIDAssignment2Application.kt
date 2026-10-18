package com.example.studentidassignment2

import android.app.Application
import com.example.studentidassignment2.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class StudentIDAssignment2Application : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@StudentIDAssignment2Application)
            modules(appModule)
        }
    }
}
