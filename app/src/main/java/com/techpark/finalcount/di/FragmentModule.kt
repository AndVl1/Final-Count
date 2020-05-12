package com.techpark.finalcount.di

import com.techpark.finalcount.history.HistoryModule
import com.techpark.finalcount.history.views.fragment.HistoryFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentModule {

	@FragmentScope
	@ContributesAndroidInjector(modules = [HistoryModule::class])
	internal abstract fun historyFragment(): HistoryFragment

}