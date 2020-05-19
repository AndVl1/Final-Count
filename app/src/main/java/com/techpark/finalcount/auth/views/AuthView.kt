package com.techpark.finalcount.auth.views

import com.techpark.finalcount.base.BaseView

interface AuthView: BaseView {
	fun showError(err: String)
	fun loginSuccess()
	fun loginError()
	fun setLoadingVisibility(vis: Boolean)
	fun isLogin(isLogin: Boolean)
}
