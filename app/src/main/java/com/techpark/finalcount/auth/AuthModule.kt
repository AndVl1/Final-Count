package com.techpark.finalcount.auth

import com.techpark.finalcount.auth.presenters.AuthPresenter
import com.techpark.finalcount.di.ActivityScope
import dagger.Binds
import dagger.Module

@Module
abstract class AuthModule {

	@ActivityScope
	@Binds
	internal abstract fun authPresenter(presenter: AuthPresenter): AuthPresenter

}