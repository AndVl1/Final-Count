package com.techpark.finalcount.history

import com.techpark.finalcount.di.FragmentScope
import com.techpark.finalcount.history.presenters.HistoryPresenter
import com.techpark.finalcount.history.presenters.HistoryPresenterImpl
import dagger.Binds
import dagger.Module

@Module
abstract class HistoryModule {

	@FragmentScope
	@Binds
	internal abstract fun historyPresenter(presenter: HistoryPresenterImpl): HistoryPresenter

}