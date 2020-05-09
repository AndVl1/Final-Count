package com.techpark.finalcount


import android.app.Application
import com.techpark.finalcount.di.AppComponent
import com.techpark.finalcount.di.DaggerAppComponent
import javax.inject.Inject

/**
 * Application Class
 *
 */
class App : Application() {

    @Inject
    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent
            .factory()
            .create(this)

        appComponent.inject(this)
    }

}