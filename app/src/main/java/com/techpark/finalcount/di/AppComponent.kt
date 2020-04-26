package com.techpark.finalcount.di

import com.techpark.finalcount.App
import com.techpark.finalcount.history.HistoryModule
import com.techpark.finalcount.purchase.PurchaseModule
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
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
        AppModule::class,
        ActivityBindingModule::class,
        AndroidSupportInjectionModule::class,
        HistoryModule::class,
        PurchaseModule::class
    ]
)
interface AppComponent : AndroidInjector<App> {

    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<App>()


}