package com.techpark.finalcount.di

import com.techpark.finalcount.adding.plan.AddingPlanningModule
import com.techpark.finalcount.adding.plan.views.fragment.AddingPlanningFragment
import com.techpark.finalcount.adding.purchase.AddingPurchaseModule
import com.techpark.finalcount.adding.purchase.views.fragment.AddingPurchaseFragment
import com.techpark.finalcount.history.HistoryModule
import com.techpark.finalcount.history.views.fragment.HistoryFragment
import com.techpark.finalcount.plans.PlansModule
import com.techpark.finalcount.plans.views.fragment.PlansFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentModule {

	@FragmentScope
	@ContributesAndroidInjector(modules = [AddingPurchaseModule::class])
	internal abstract fun addingFragment(): AddingPurchaseFragment

	@FragmentScope
	@ContributesAndroidInjector(modules = [HistoryModule::class])
	internal abstract fun historyFragment(): HistoryFragment

	@FragmentScope
	@ContributesAndroidInjector(modules = [PlansModule::class])
	internal abstract fun plansFragment(): PlansFragment

	@FragmentScope
	@ContributesAndroidInjector(modules = [AddingPlanningModule::class])
	internal abstract fun planningFragment(): AddingPlanningFragment
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