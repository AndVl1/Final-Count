package com.techpark.finalcount.di

import android.content.Context
import com.techpark.finalcount.App
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

/**
 * Application component consisting of
 * App Module, Activity Binding module.
 * The Scope is Singleton it life will be a
 * active entire application
 *
 */
@Singleton
@Component(
    modules = [
	    AndroidInjectionModule::class,
        ActivityBindingModule::class,
	    FragmentModule::class
//        HistoryModule::class,
//        PurchaseModule::class
    ]
)
interface AppComponent /*: AndroidInjector<App>*/ {

//    @Component.Builder
//    abstract class Builder : AndroidInjector.Builder<App>()

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun inject(app: App)

}