package com.techpark.finalcount.plans

import androidx.lifecycle.ViewModel
import com.techpark.finalcount.di.FragmentScope
import com.techpark.finalcount.history.viewmodel.HistoryViewModel
import dagger.Binds
import dagger.Module

@Module
abstract class PlansModule {
	@FragmentScope
	@Binds
	internal abstract fun plansViewModel(viewModel: HistoryViewModel): ViewModel
}