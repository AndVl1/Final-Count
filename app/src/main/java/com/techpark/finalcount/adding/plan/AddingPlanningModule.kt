package com.techpark.finalcount.adding.plan

import com.techpark.finalcount.di.FragmentScope
import com.techpark.finalcount.adding.plan.presenter.AddingPlanningPresenter
import dagger.Binds
import dagger.Module

@Module
abstract class AddingPlanningModule {
	@FragmentScope
	@Binds
	internal abstract fun planningPresenter(presenter: AddingPlanningPresenter): AddingPlanningPresenter
}