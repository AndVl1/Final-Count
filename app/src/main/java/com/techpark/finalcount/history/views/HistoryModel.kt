package com.techpark.finalcount.history.views

import com.techpark.finalcount.di.FragmentScope
import com.techpark.finalcount.history.presenters.HistoryPresenter
import com.techpark.finalcount.history.presenters.HistoryPresenterImplementation
import dagger.Binds
import dagger.Module

@Module
abstract class HistoryModule {

    @FragmentScope
    @Binds
    internal abstract fun historyPresenter(presenter: HistoryPresenterImplementation): HistoryPresenter

}