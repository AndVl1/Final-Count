package com.techpark.finalcount.main

import com.techpark.finalcount.di.ActivityScope
import com.techpark.finalcount.main.presenters.MainPresenter
import com.techpark.finalcount.main.presenters.MainPresenterImpl
import dagger.Binds
import dagger.Module

@Module
abstract class MainModule {

	@ActivityScope
	@Binds
	internal abstract fun mainPresenter(presenter: MainPresenterImpl): MainPresenter

}