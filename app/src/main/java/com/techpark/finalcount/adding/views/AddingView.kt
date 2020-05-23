package com.techpark.finalcount.adding.views

import com.techpark.finalcount.base.BaseView

interface AddingView: BaseView {
	fun showError(error: String)
	fun addSuccess()
	fun addFailed()
	fun setLoadingVisibility(visibility: Boolean)
}