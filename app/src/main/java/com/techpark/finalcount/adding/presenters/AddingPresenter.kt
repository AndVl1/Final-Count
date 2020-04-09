package com.techpark.finalcount.adding.presenters

import com.techpark.finalcount.adding.views.AddingView
import com.techpark.finalcount.base.BasePresenter
import com.techpark.finalcount.database.model.Purchase

interface AddingPresenter: BasePresenter<AddingView> {
    fun check()
    fun add(purchase: Purchase)

}