package com.techpark.finalcount.di

import com.techpark.finalcount.adding.AddingModule
import com.techpark.finalcount.adding.views.activity.AddingFragment
import com.techpark.finalcount.history.HistoryModule
import com.techpark.finalcount.history.views.fragment.HistoryFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentModule {

	@FragmentScope
	@ContributesAndroidInjector(modules = [HistoryModule::class])
	internal abstract fun historyFragment(): HistoryFragment

	@FragmentScope
	@ContributesAndroidInjector(modules = [AddingModule::class])
	internal abstract fun addingFragment(): AddingFragment

	/**
	 * To create new fragment where you can inject:
	 *
	 * Step 1: create Module and Component file line
	 * in "history" package
	 *
	 * Step 2: add your module here
	 *
	 * Step 3: AndroidSupportInjection.inject(this) in
	 * onAttach() method before super.onAttach()
	 *
	 * ... ?
	 *
	 * profit!
	 *
	 * */
}