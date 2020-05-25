package com.techpark.finalcount.adding.purchase

import com.techpark.finalcount.adding.purchase.presenters.AddingPurchasePresenter
import com.techpark.finalcount.di.FragmentScope
import dagger.Binds
import dagger.Module

@Module
abstract class AddingPurchaseModule {
	@FragmentScope
	@Binds
	internal abstract fun addingPresenter(presenter: AddingPurchasePresenter): AddingPurchasePresenter
}