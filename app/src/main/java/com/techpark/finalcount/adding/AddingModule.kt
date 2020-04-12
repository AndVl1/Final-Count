package com.techpark.finalcount.adding

import com.techpark.finalcount.adding.presenters.AddingPresenter
import com.techpark.finalcount.adding.presenters.AddingPresenterImplementation
import com.techpark.finalcount.di.ActivityScope
import dagger.Binds
import dagger.Module

@Module
abstract class AddingModule {

    @ActivityScope
    @Binds
    internal abstract fun addingPresenter(presenter: AddingPresenterImplementation): AddingPresenter

}