package com.techpark.finalcount.di

import com.techpark.finalcount.adding.AddingModule
import com.techpark.finalcount.adding.views.activity.AddingActivity
import com.techpark.finalcount.auth.AuthModule
import com.techpark.finalcount.auth.views.activity.AuthActivity
import com.techpark.finalcount.auth.views.activity.AuthActivityTesting
import com.techpark.finalcount.main.MainModule
import com.techpark.finalcount.main.views.activity.MainActivity
import com.techpark.finalcount.purchase.PurchaseModule
import com.techpark.finalcount.purchase.view.activity.PurchaseActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Activity Binding module, it uses @ContributesAndroidInjector.
 * It will be used to create subComponent Scope Instance and  the life
 * of the scope instance which like as long as the activity & Fragment.
 */
@Module
abstract class ActivityModule {

	@ActivityScope
	@ContributesAndroidInjector(modules = [AddingModule::class])
	internal abstract fun addingActivity(): AddingActivity

	@ActivityScope
	@ContributesAndroidInjector(modules = [PurchaseModule::class])
	internal abstract fun purchaseActivity(): PurchaseActivity

	@ActivityScope
	@ContributesAndroidInjector(modules = [MainModule::class])
	internal abstract fun mainActivity(): MainActivity

	@ActivityScope
	@ContributesAndroidInjector(modules = [AuthModule::class])
	internal abstract fun authActivity(): AuthActivity

	@ActivityScope
	@ContributesAndroidInjector(modules = [AuthModule::class])
	internal abstract fun authActivityTesting(): AuthActivityTesting

	/**
	 * To create new activity where you can inject:
	 *
	 * Step 1: create Module file like
	 * in "adding" package
	 *
	 * Step 2: add your module here
	 *
	 * Step 3: AndroidInjection.inject(this) in
	 * onCreate() method before super.onCreate()
	 *
	 * ... ?
	 *
	 * profit!
	 *
	 * */

}