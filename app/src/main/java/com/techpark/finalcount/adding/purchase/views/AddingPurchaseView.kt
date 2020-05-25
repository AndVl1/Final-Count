package com.techpark.finalcount.adding.purchase.views

import com.techpark.finalcount.base.BaseView

interface AddingPurchaseView: BaseView {
	fun showError(error: String)
	fun addSuccess()
	fun addFailed()
	fun setLoadingVisibility(visibility: Boolean)
}