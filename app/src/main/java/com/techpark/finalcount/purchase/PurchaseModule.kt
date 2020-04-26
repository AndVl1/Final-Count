package com.techpark.finalcount.purchase

import com.techpark.finalcount.di.ActivityScope
import com.techpark.finalcount.purchase.presenter.PurchasePresenter
import dagger.Binds
import dagger.Module

@Module
abstract class PurchaseModule {

    @ActivityScope
    @Binds
    internal abstract fun purchasePresenter(presenter: PurchasePresenter): PurchasePresenter
}