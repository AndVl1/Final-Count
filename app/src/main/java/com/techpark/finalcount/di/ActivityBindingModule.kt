package com.techpark.finalcount.di

import com.techpark.finalcount.adding.AddingModule
import com.techpark.finalcount.adding.views.activity.AddingActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Activity Binding module, it uses @ContributesAndroidInjector.
 * It will be used to create subComponent Scope Instance and  the life
 * of the scope instance which like as long as the activity & Fragment.
 */
@Module
abstract class ActivityBindingModule {

    @ActivityScope
    @ContributesAndroidInjector(modules = [AddingModule::class])
    internal abstract fun addingActivity(): AddingActivity

}