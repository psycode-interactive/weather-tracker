package com.psycodeinteractive.weathertracker

import android.app.Application
import com.squareup.leakcanary.core.BuildConfig
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class WeatherTrackerApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.Forest.plant(Timber.DebugTree())
        }
    }
}
