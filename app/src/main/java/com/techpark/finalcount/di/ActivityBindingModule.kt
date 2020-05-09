package com.techpark.finalcount.di

/**
 * Activity Binding module, it uses @ContributesAndroidInjector.
 * It will be used to create subComponent Scope Instance and  the life
 * of the scope instance which like as long as the activity & Fragment.
 */
//@Module
//abstract class ActivityBindingModule {
//
//    @ActivityScope
//    @ContributesAndroidInjector(modules = [AddingModule::class])
//    internal abstract fun addingActivity(): AddingActivity
//
//    @ActivityScope
//    @ContributesAndroidInjector(modules = [PurchaseModule::class])
//    internal abstract fun purchaseActivity(): PurchaseActivity
//
//    @FragmentScope
//    @ContributesAndroidInjector(modules = [HistoryModule::class])
//    internal abstract fun historyFragment(): HistoryFragment
//
//}