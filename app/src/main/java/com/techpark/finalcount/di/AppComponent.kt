package com.techpark.finalcount.di

import android.content.Context
import com.techpark.finalcount.App
import com.techpark.finalcount.base.BaseActivity
import com.techpark.finalcount.history.HistoryComponent
import com.techpark.finalcount.history.HistoryModule
import dagger.BindsInstance
import dagger.Component
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
        AppModule::class
//        ActivityBindingModule::class
//        AndroidSupportInjectionModule::class
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
    fun inject(activity: BaseActivity)

    fun historyComponent(historyModule: HistoryModule): HistoryComponent
}