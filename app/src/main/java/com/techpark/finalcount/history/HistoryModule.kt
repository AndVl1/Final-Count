package com.techpark.finalcount.history

import androidx.lifecycle.ViewModel
import com.techpark.finalcount.di.FragmentScope
import com.techpark.finalcount.history.viewmodel.HistoryViewModel
import dagger.Binds
import dagger.Module

@Module
abstract class HistoryModule {

	@FragmentScope
	@Binds
	internal abstract fun historyViewModel(viewModel: HistoryViewModel): ViewModel
}