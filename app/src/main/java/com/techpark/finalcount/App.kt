package com.techpark.finalcount


import android.app.Application
import com.techpark.finalcount.data.PinPreferences
import com.techpark.finalcount.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

/**
 * Application Class
 *
 */
class App : Application(), HasAndroidInjector {
	val mPinPrefs = PinPreferences(this)

	@Inject
	lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

	override fun onCreate() {
		super.onCreate()

		DaggerAppComponent
			.factory()
			.create(this)
			.inject(this)
	}

	override fun androidInjector(): AndroidInjector<Any> = dispatchingAndroidInjector

}