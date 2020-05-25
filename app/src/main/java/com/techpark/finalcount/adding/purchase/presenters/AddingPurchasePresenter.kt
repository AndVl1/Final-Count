package com.techpark.finalcount.adding.purchase.presenters

import com.techpark.finalcount.adding.purchase.views.AddingPurchaseView
import com.techpark.finalcount.base.BasePresenter

interface AddingPurchasePresenter : BasePresenter<AddingPurchaseView> {
	fun add(name: String, cost: Int, currency: String)
}