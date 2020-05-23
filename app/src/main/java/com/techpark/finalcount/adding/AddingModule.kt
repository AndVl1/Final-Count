package com.techpark.finalcount.adding

import com.techpark.finalcount.adding.presenters.AddingPresenter
import com.techpark.finalcount.di.ActivityScope
import com.techpark.finalcount.di.FragmentScope
import dagger.Binds
import dagger.Module

@Module
abstract class AddingModule {

	@ActivityScope
	@Binds
	internal abstract fun addingPresenter(presenter: AddingPresenter): AddingPresenter

	@FragmentScope
	@Binds
	internal abstract fun addingFrPresenter(presenter: AddingPresenter): AddingPresenter
}