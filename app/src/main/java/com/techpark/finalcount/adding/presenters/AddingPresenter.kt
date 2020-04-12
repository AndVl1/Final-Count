package com.techpark.finalcount.adding.presenters

import com.techpark.finalcount.adding.views.AddingView
import com.techpark.finalcount.base.BasePresenter

interface AddingPresenter : BasePresenter<AddingView> {
    fun add(name: String, cost: Int, currency: Int)
    fun check() : String
}